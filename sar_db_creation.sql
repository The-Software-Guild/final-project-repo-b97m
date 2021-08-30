DROP SCHEMA IF EXISTS sarDB;
CREATE SCHEMA sarDB;
USE sarDB;

CREATE TABLE request (
	requestId INT NOT NULL AUTO_INCREMENT,
	requestText VARCHAR(1000) NOT NULL,
	requestTime DATETIME NOT NULL DEFAULT NOW(),
    CONSTRAINT PK_request PRIMARY KEY (requestId)
);

CREATE TABLE article (
	articleId INT NOT NULL AUTO_INCREMENT,
	articleAuthor VARCHAR(200),
    articleSource VARCHAR(200),
    articleTitle VARCHAR(500),
    articleUrl VARCHAR(200),
    articlePublicationTime DATETIME,
    articleSavingTime DATETIME NOT NULL DEFAULT NOW(),
	CONSTRAINT PK_article PRIMARY KEY (articleId)
);

CREATE TABLE review (
	reviewId INT NOT NULL AUTO_INCREMENT,
    reviewCreationTime DATETIME NOT NULL DEFAULT NOW(),
    reviewUpdateTime DATETIME NOT NULL DEFAULT NOW(),
    reviewText VARCHAR(1000) NOT NULL,
	articleId INT NOT NULL,
    CONSTRAINT PK_review PRIMARY KEY (reviewId),
    CONSTRAINT FK_review_article FOREIGN KEY (articleId)
		REFERENCES article (articleId)
);