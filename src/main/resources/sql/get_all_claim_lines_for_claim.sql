CREATE OR REPLACE PROCEDURE public.get_all_claim_lines_for_claim(
    in inputValue varchar, inout outputValue varchar)
    language plpgsql
AS
$BODY$
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
        array_to_json(array_agg(clex)) as "exCodeBeanList"
        FROM (select ex_code_id as "exCode", confidence_score as "confidence", claimline_id FROM claimline_excode) clex
        JOIN claim_line cl ON cl.claimline_id = clex.claimline_id
        WHERE cl.claim_num = NULLIF(reqJson->>'claimNum', '')::numeric
        GROUP BY cl.claimline_id, cl.claim_line_num, cl.approver_level_one, cl.approver_level_two,
        cl.approver_level_three,
        cl.approver_level_four) u;
       outputValue  := (SELECT cast(resJson AS varchar));
END;
$BODY$;