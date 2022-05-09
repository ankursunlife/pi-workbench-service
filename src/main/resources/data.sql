delete from public.user_details;

INSERT INTO public.user_details (email_id, created_by, created_time, image_url, updated_by, updated_time, user_name, user_type) 
VALUES('akadam@aarete.com', 'pi-workbench-service', NOW(), '', 'pi-workbench-service', NOW(), 'Anup Kadam', 'AARETE_MANAGER');

INSERT INTO public.user_details (email_id, created_by, created_time, image_url, updated_by, updated_time, user_name, user_type) 
VALUES('rkamboj@aarete.com', 'pi-workbench-service', NOW(), '', 'pi-workbench-service', NOW(), 'Rahul Kamboj', 'AARETE_USER');
