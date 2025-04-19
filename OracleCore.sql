CREATE TABLE students (
    id NUMBER PRIMARY KEY,               
    full_name VARCHAR2(50) NOT NULL,       
    birth_date DATE,  
    phone VARCHAR(13),
    email VARCHAR2(100)                  
);

CREATE SEQUENCE students_seq
    START WITH 1                  -- Giá trị bắt đầu là 1
    INCREMENT BY 1               -- Tăng lên 1 mỗi lần
    NOCACHE;                      -- Không lưu trữ giá trị trong bộ nhớ

-- tạo tài khoản khác sys
SELECT owner, table_name FROM dba_tables WHERE table_name = 'STUDENT';
CREATE USER hr_test IDENTIFIED BY 123456;
GRANT CREATE SESSION TO hr_test;
GRANT CREATE TABLE TO hr_test;
CONNECT hr_test/123456@//localhost:1521/ORCL

CREATE TABLE hr_test.students AS SELECT * FROM sys.students;
SELECT * FROM hr_test.students;

-- tạo trigger
CREATE OR REPLACE TRIGGER student_id_trigger
BEFORE INSERT ON hr_test.students
FOR EACH ROW
BEGIN
    :NEW.id := students_seq.NEXTVAL;
END;

-- cấp quota trong tablespace
SELECT table_name, tablespace_name
FROM all_tables
WHERE table_name = 'STUDENTS' AND owner = 'HR_TEST';

ALTER USER hr_test QUOTA UNLIMITED ON users;

INSERT INTO hr_test.students (full_name, birth_date, phone, email)
VALUES ('Quoc Viet', TO_DATE('2003-01-01', 'YYYY-MM-DD'), '0366827983', 'vietddls06@gmail.com');

-- insert nhieu
INSERT ALL
  INTO hr_test.students (full_name, birth_date, phone, email)
  VALUES ('Nguyen Thi Hoa', TO_DATE('2002-05-15', 'YYYY-MM-DD'), '0912345678', 'hoa.nguyen2002@gmail.com')
  INTO hr_test.students (full_name, birth_date, phone, email)
  VALUES ('Le Van Nam', TO_DATE('2001-09-20', 'YYYY-MM-DD'), '0987654321', 'nam.levan2001@gmail.com')
  INTO hr_test.students (full_name, birth_date, phone, email)
  VALUES ('Tran Minh Tu', TO_DATE('2003-03-08', 'YYYY-MM-DD'), '0377888999', 'minhtu0308@gmail.com')
  INTO hr_test.students (full_name, birth_date, phone, email)
  VALUES ('Pham Bao Chau', TO_DATE('2000-12-01', 'YYYY-MM-DD'), '0909090909', 'chaupham2000@yahoo.com')
  INTO hr_test.students (full_name, birth_date, phone, email)
  VALUES ('Doan Gia Bao', TO_DATE('2004-07-27', 'YYYY-MM-DD'), '0388888999', 'giabao.doan2004@hotmail.com')
SELECT * FROM dual;

select * from hr_test.students;

PARTITION BY RANGE (birth_date) (
    PARTITION p2000 VALUES LESS THAN (TO_DATE('01-JAN-2001', 'DD-MON-YYYY')),
    PARTITION p2001 VALUES LESS THAN (TO_DATE('01-JAN-2002', 'DD-MON-YYYY')),
    PARTITION p2002 VALUES LESS THAN (TO_DATE('01-JAN-2003', 'DD-MON-YYYY')),
    PARTITION p2003 VALUES LESS THAN (TO_DATE('01-JAN-2004', 'DD-MON-YYYY')),
    PARTITION p2004 VALUES LESS THAN (TO_DATE('01-JAN-2005', 'DD-MON-YYYY'))
);


-- partition theo năm sinh
CREATE TABLE students_partitioned (
    id NUMBER PRIMARY KEY,               
    full_name VARCHAR2(50) NOT NULL,       
    birth_date DATE,  
    phone VARCHAR(13),
    email VARCHAR2(100)
)
PARTITION BY RANGE (birth_date) (
    PARTITION p2000 VALUES LESS THAN (TO_DATE('01-JAN-2001', 'DD-MON-YYYY')),
    PARTITION p2001 VALUES LESS THAN (TO_DATE('01-JAN-2002', 'DD-MON-YYYY')),
    PARTITION p2002 VALUES LESS THAN (TO_DATE('01-JAN-2003', 'DD-MON-YYYY')),
    PARTITION p2003 VALUES LESS THAN (TO_DATE('01-JAN-2004', 'DD-MON-YYYY')),
    PARTITION p2004 VALUES LESS THAN (TO_DATE('01-JAN-2005', 'DD-MON-YYYY'))
);

-- du lieu ban cu sang bang partition
INSERT INTO students_partitioned (id, full_name, birth_date, phone, email)
SELECT id, full_name, birth_date, phone, email
FROM students;

--job
BEGIN
  DBMS_SCHEDULER.create_job (
    job_name        => 'delete_old_students',  
    job_type        => 'PLSQL_BLOCK',          
    job_action      => 'BEGIN DELETE FROM students WHERE TRUNC(MONTHS_BETWEEN(SYSDATE, birth_date) / 12) > 30; END;',  -- proceduce thuc thi
    start_date      => SYSTIMESTAMP,             -- Thời gian bắt đầu ngay lập tức
    repeat_interval => 'FREQ=DAILY; BYHOUR=9; BYMINUTE=0; BYSECOND=0;', -- Lịch trình: Mỗi ngày lúc 00:00
    enabled         => TRUE                       -- Kích hoạt job ngay lập tức
  );
END;

--procedure
CREATE OR REPLACE PROCEDURE delete_students_older_than_30 AS
BEGIN
  DELETE FROM students
  WHERE TRUNC(MONTHS_BETWEEN(SYSDATE, birth_date) / 12) > 30;
  
  COMMIT; 
END delete_students_older_than_30;

EXEC delete_students_older_than_30;



