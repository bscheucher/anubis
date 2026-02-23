ALTER TABLE moxis_job
DROP constraint moxis_job_constituent_fkey;
     ALTER table moxis_job
ALTER COLUMN constituent TYPE text;

ALTER TABLE moxis_job_history
ALTER COLUMN constituent TYPE text;