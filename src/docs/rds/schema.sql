DROP TABLE IF EXISTS MST_USER CASCADE;
CREATE TABLE MST_USER(
    USER_KEY            BIGINT              GENERATED BY DEFAULT AS IDENTITY,
    ACTIVATE_STATUS     VARCHAR(64)         NOT NULL,

    DELETED             BOOLEAN             NOT NULL,
    CREATED_BY          BIGINT,
    CREATED_AT          TIMESTAMP           NOT NULL,
    LAST_MODIFIED_BY    BIGINT,
    LAST_MODIFIED_AT    TIMESTAMP           NOT NULL,
    PRIMARY KEY(USER_KEY)
);

DROP TABLE IF EXISTS MST_USER_ROLE CASCADE;
CREATE TABLE MST_USER_ROLE(
    USER_ROLE_KEY       BIGINT              GENERATED BY DEFAULT AS IDENTITY,
    ROLE_TYPE           VARCHAR(64)         NOT NULL,
    USER_KEY            BIGINT              NOT NULL,

    DELETED             BOOLEAN             NOT NULL,
    CREATED_BY          BIGINT,
    CREATED_AT          TIMESTAMP           NOT NULL,
    LAST_MODIFIED_BY    BIGINT,
    LAST_MODIFIED_AT    TIMESTAMP           NOT NULL,
    PRIMARY KEY(USER_ROLE_KEY)
);

DROP TABLE IF EXISTS SOCIAL_USER_IDENTI CASCADE;
CREATE TABLE SOCIAL_USER_IDENTI(
    SOCIAL_IDENT_KEY    BIGINT              GENERATED BY DEFAULT AS IDENTITY,
    IDENTIFICATION      VARCHAR(1024)       NOT NULL,
    SOCIAL_TYPE         VARCHAR(64)         NOT NULL,
    USER_KEY            BIGINT              NOT NULL,
    EMAIL               VARCHAR(128),

    DELETED             BOOLEAN             NOT NULL,
    CREATED_BY          BIGINT,
    CREATED_AT          TIMESTAMP           NOT NULL,
    LAST_MODIFIED_BY    BIGINT,
    LAST_MODIFIED_AT    TIMESTAMP           NOT NULL,
    PRIMARY KEY(SOCIAL_IDENT_KEY)
);

DROP TABLE IF EXISTS MST_USER_CREDENTIAL CASCADE;
CREATE TABLE MST_USER_CREDENTIAL(
    CREDENTIAL_KEY          BIGINT              GENERATED BY DEFAULT AS IDENTITY,
    PASSWORD                VARCHAR(256)        NOT NULL,
    USER_KEY                BIGINT              NOT NULL,

    DELETED                 BOOLEAN             NOT NULL,
    CREATED_BY              BIGINT,
    CREATED_AT              TIMESTAMP           NOT NULL,
    LAST_MODIFIED_BY        BIGINT,
    LAST_MODIFIED_AT        TIMESTAMP           NOT NULL,
    PRIMARY KEY(CREDENTIAL_KEY)
);

DROP TABLE IF EXISTS MST_USER_DETAILS CASCADE;
CREATE TABLE MST_USER_DETAILS(
    USER_DETAILS_KEY        BIGINT              GENERATED BY DEFAULT AS IDENTITY,
    NICKNAME                VARCHAR(128)        NOT NULL,
    STR_ID                  UUID                NOT NULL,
    USER_KEY                BIGINT              NOT NULL,

    DELETED                 BOOLEAN             NOT NULL,
    CREATED_BY              BIGINT,
    CREATED_AT              TIMESTAMP           NOT NULL,
    LAST_MODIFIED_BY        BIGINT,
    LAST_MODIFIED_AT        TIMESTAMP           NOT NULL,
    PRIMARY KEY(USER_DETAILS_KEY)
);

DROP TABLE IF EXISTS EMAIL_SIGNUP_AUTH CASCADE;
CREATE TABLE EMAIL_SIGNUP_AUTH(
    AUTH_KEY                BIGINT              GENERATED BY DEFAULT AS IDENTITY,
    USER_KEY                BIGINT              NOT NULL,
    UUID                    UUID                NOT NULL,

    DELETED                 BOOLEAN             NOT NULL,
    CREATED_BY              BIGINT,
    CREATED_AT              TIMESTAMP           NOT NULL,
    LAST_MODIFIED_BY        BIGINT,
    LAST_MODIFIED_AT        TIMESTAMP           NOT NULL,
    PRIMARY KEY(AUTH_KEY)
);

DROP TABLE IF EXISTS EMAIL_TEMPLATE CASCADE;
CREATE TABLE EMAIL_TEMPLATE(
    TEMPLATE_KEY            BIGINT              GENERATED BY DEFAULT AS IDENTITY,
    TITLE                   VARCHAR(128)        NOT NULL,
    TEMPLATE                TEXT                NOT NULL,
    TEMPLATE_TYPE           VARCHAR(64)         NOT NULL,

    DELETED                 BOOLEAN             NOT NULL,
    CREATED_BY              BIGINT,
    CREATED_AT              TIMESTAMP           NOT NULL,
    LAST_MODIFIED_BY        BIGINT,
    LAST_MODIFIED_AT        TIMESTAMP           NOT NULL,
    PRIMARY KEY(TEMPLATE_KEY)
);

/* CA-27 */
DROP TABLE IF EXISTS MST_USER_LOGIN_LOG CASCADE;
CREATE TABLE MST_USER_LOGIN_LOG(
    LOG_KEY                 BIGINT              GENERATED BY DEFAULT AS IDENTITY,
    USER_KEY                BIGINT              NOT NULL,
    STATUS                  VARCHAR(128)        NOT NULL,

    DELETED                 BOOLEAN             NOT NULL,
    CREATED_BY              BIGINT,
    CREATED_AT              TIMESTAMP           NOT NULL,
    LAST_MODIFIED_BY        BIGINT,
    LAST_MODIFIED_AT        TIMESTAMP           NOT NULL,
    PRIMARY KEY(LOG_KEY)
);