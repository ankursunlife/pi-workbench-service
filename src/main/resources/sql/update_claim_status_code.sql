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
END;
$procedure$
;
