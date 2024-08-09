DROP TABLE IF EXISTS TB_USERS;

CREATE TABLE TB_USERS
(
    MEMBER_ID       INT AUTO_INCREMENT NOT NULL,
    MEMBER_LOGIN_ID VARCHAR(20) NOT NULL,
    MEMBER_ROLE     VARCHAR(20) DEFAULT 'USER',
    MEMBER_NAME     VARCHAR(20) NOT NULL,
    MEMBER_PASSWORD VARCHAR(400) NOT NULL,
    MEMBER_EMAIL    VARCHAR(100) NOT NULL,
    IS_USED         CHAR(1) DEFAULT 'Y',
    IS_DEL          CHAR(1) DEFAULT 'N',
    SYS_DATE       DATETIME DEFAULT NOW(),
    REG_DATE       DATETIME ,
    CONSTRAINT USERS_PK PRIMARY KEY (MEMBER_ID)
);

-- 비밀번호 test
INSERT INTO TB_USERS (MEMBER_LOGIN_ID, MEMBER_NAME, MEMBER_PASSWORD, MEMBER_EMAIL)
VALUES ('member1', '테스트1', '$2a$10$PoHdBEq87U332s9HxnXHfOkk3SZyGVNJz2YsPEi3Q3T9.O1.UzrIC', 'all_step@naver.com');

-- 비밀번호 1234
INSERT INTO TB_USERS (MEMBER_LOGIN_ID, MEMBER_NAME, MEMBER_PASSWORD, MEMBER_EMAIL)
VALUES ('member2', '테스트2', '$2a$12$R0ZgpAnBKh8CX0sATNRY8OyXPfke6GsXOxOA18gWyJ7RrnzOGnDOu', '');