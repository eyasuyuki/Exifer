
# -----------------------------------------------------------------------
# exifmap
# -----------------------------------------------------------------------
drop table if exists exifmap;

CREATE TABLE exifmap
(
    id INTEGER NOT NULL,
    path VARCHAR(8196) NOT NULL,
    name VARCHAR(2048) NOT NULL,
    exifdate TIMESTAMP,
    filedate TIMESTAMP,
    size BIGINT NOT NULL,
    ctime TIMESTAMP,
    mtime TIMESTAMP,
    PRIMARY KEY(id));

