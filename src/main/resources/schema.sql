CREATE OR REPLACE PROCEDURE public.workflow_claim_update(in_json_txt json, INOUT errorcode integer, INOUT errormsg text)
    LANGUAGE plpgsql
AS $procedure$

DECLARE
    claimline_json json ;
    claimline_ele_json json;
    claimline_levellist_json json;
    claimline_level_json json;
    exCodeBean_json json;
    exCodeBeanList_json json;
    claimLineNum integer;
    batchId integer;
    claimLineId integer;
    claimLine_Level varchar;
    approverOneAction varchar;
    approverTwoAction varchar;
    approverThreeAction varchar ;
    approverFourAction varchar;
    exCode varchar;
    confidenceScore float;
    claimStatusCode varchar;
    jsonElementLength int;
    isActive varchar;
    exCode_claimLine varchar;
    confidenceScore_claimLine float;
    opportunity_amount float;
    actionTaken varchar;
    actionTakenBy varchar;
    approverLevel varchar;

BEGIN

    approverLevel := to_json(in_json_txt) #>>'{approverLevel}';
    actionTaken := to_json(in_json_txt) #>>'{actionTaken}';
    actionTakenBy := to_json(in_json_txt) #>>'{actionTakenBy}';

    SELECT in_json_txt #> '{claimlines}' into claimline_json;

    FOR claimline_ele_json IN SELECT * FROM json_array_elements(claimline_json)
        loop
            --get the batchid,claimlineNum,claimStatusCode and claimLineId
            claimLineId := claimline_ele_json ->'claimLineId';
            claimLineNum := claimline_ele_json ->'claimLineNum';
            batchId := claimline_ele_json ->'claimLineNum';
            claimStatusCode := claimline_ele_json ->'claimStatusCode';



            --******************update claimline_excode table********************************
            exCodeBeanList_json := (claimline_ele_json ->'exCodeBeanList');

            --Validation
            if exCodeBeanList_json IS null then
                errorcode := 400;
                errormsg := 'Excode Bean List is missing in input string.';
                exit;
            end if;

            jsonElementLength := json_array_length(exCodeBeanList_json);
            if jsonElementLength = 0 then
                errorcode := 400;
                errormsg := 'Excode Bean List is missing in input string.';
                exit;
            end if;


            --RAISE NOTICE 'Length %', jsonElementLength;
            FOR exCodeBean_json IN  SELECT * from json_array_elements(exCodeBeanList_json)
                loop

                    isActive  := exCodeBean_json ->>'isactive';
                    if isActive = 'true' then
                        exCode_claimLine := exCodeBean_json ->>'exCode';
                        confidenceScore_claimLine := exCodeBean_json ->>'confidence';
                        --opportunity_amount := exCodeBean_json ->>'confidence';
                    end if;

                    exCode := exCodeBean_json ->>'exCode';
                    confidenceScore := exCodeBean_json ->>'confidence';
                    --We dont have actiontaken column in excode_level table.Please confirm as its coming from json and mention in excel

                    UPDATE public.claimline_excode
                    SET --confidence_score=confidenceScore,
                        approver_one_action = case approverLevel WHEN 'AARETE_USER' THEN actiontaken  else approver_one_action end,
                        approver_two_action = case approverLevel WHEN 'AARETE_MANAGER' THEN actiontaken else approver_two_action end,
                        approver_three_action = case approverLevel WHEN 'CLIENT_USER' THEN actiontaken else approver_three_action end,
                        approver_four_action = CASE approverLevel WHEN 'CLIENT_MANAGER' THEN actiontaken else approver_four_action end
                        --,action_taken = ?
                    WHERE 	batch_id = batchId
                      AND claim_line_num = claimLineNum
                      AND claimline_id = claimLineId
                      AND ex_code_id = exCode;

                end loop;

            --**********************************************************************************************

            claimline_levellist_json := (claimline_ele_json ->'claimlineLevelList');

            if claimline_levellist_json IS null then
                errorcode := 400;
                errormsg := 'ClaimLine Level List is missing in input string.';
                exit;
            end if;

            jsonElementLength := json_array_length(claimline_levellist_json);

            if jsonElementLength = 0 then
                errorcode := 400;
                errormsg := 'ClaimLine Level List is missing in input string.';
                exit;
            end if;

            FOR claimline_level_json IN  SELECT * from json_array_elements(claimline_levellist_json)
                loop

                    claimLine_Level := claimline_level_json ->> 'level';
                    --RAISE NOTICE 'Level %', claimLine_Level;
                    case claimLine_Level
                        when 'ONE' then
                            approverOneAction := claimline_level_json ->> 'status';
                        when 'TWO' then
                            approverTwoAction := claimline_level_json ->> 'status';
                        when 'THREE' then
                            approverThreeAction := claimline_level_json ->> 'status';
                        else
                            approverFourAction := claimline_level_json ->> 'status';
                        end case;

                end loop;

            UPDATE public.claim_line
            SET 	approver_four_action=approverFourAction,
                   approver_one_action=approverOneAction,
                   approver_three_action=approverThreeAction,
                   approver_two_action=approverTwoAction,
                   claim_status_cd= claimStatusCode,
                   --opp_line_paid_amt = CASE WHEN (varApproverLevel ='AARETE_USER') THEN varApproverAction else approver_one_action end,
                   confidence_score = CASE isActive WHEN 'true' THEN confidenceScore_claimLine else confidence_score end

            WHERE 	batch_id = batchId
              AND claim_line_num = claimLineNum
              AND claimline_id = claimLineId;

        END LOOP;

    errorcode :=0;
    errormsg := 'SUCCESS';
	commit;
EXCEPTION WHEN OTHERS THEN
    errorcode :=1;
    errormsg := sqlerrm;
    ROLLBACK;
END;

$procedure$
;


CREATE OR REPLACE PROCEDURE public.update_claim_status_code(statusjson character varying)
    LANGUAGE plpgsql
AS $procedure$
DECLARE
    i json;
    json_value json;
begin
    json_value := (SELECT cast(statusJson AS json));
    FOR i IN (SELECT json_array_elements(json_value))
        LOOP
            UPDATE claim_line set claim_status_cd = i->>'claimStatusCode' where claimline_id = NULLIF(i->>'claimLineId', '')::numeric;
    END LOOP;
    COMMIT;
EXCEPTION WHEN OTHERS THEN
    ROLLBACK;
END;
$procedure$
;


CREATE OR REPLACE PROCEDURE public.get_all_claim_lines_for_claim(IN inputvalue character varying, INOUT outputvalue character varying)
 LANGUAGE plpgsql
AS $procedure$
declare
    reqJson json;
    resJson json;
begin
    reqJson := (SELECT cast(inputValue AS json));
    select json_build_object('claimlines', array_to_json(array_agg(u))) into resJson
    from (
             SELECT cl.claimline_id         as "claimLineId",
                    cl.claim_line_num              as "claimLineNum",
                    cl.approver_level_one          as "approverLevelOne",
                    cl.approver_level_two          as "approverLevelTwo",
                    cl.approver_level_three        as "approverLevelThree",
                    cl.approver_level_four         as "approverLevelFour",
                    array_to_json(array_agg(clex)) as "exCodeBeanList",
                    batch_id as "batchID"
             FROM (select 	ex_code_id as "exCodeId", confidence_score as "confidence", claimline_id,( CASE WHEN prioritization_score IS NULL THEN 0	ELSE prioritization_score END) as "prioritizationScore",
             				ex_code_type as "exCodeType",( CASE WHEN order_rating IS NULL THEN 0	ELSE order_rating END) as "orderRating" 
             		FROM claimline_excode) clex
                      JOIN claim_line cl ON cl.claimline_id = clex.claimline_id
             WHERE cl.claim_num = NULLIF(reqJson->>'claimNum', '')::numeric
             GROUP BY cl.claimline_id, cl.claim_line_num, cl.approver_level_one, cl.approver_level_two,
                      cl.approver_level_three,
                      cl.approver_level_four) u;
    outputValue  := (SELECT cast(resJson AS varchar));
END;
$procedure$
;