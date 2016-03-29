CREATE TABLE `Questions` (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, lang INTEGER NOT NULL DEFAULT "0" , type INTEGER NOT NULL DEFAULT "1" , level INTEGER NOT NULL DEFAULT "1" , levelA CHAR(5) NOT NULL DEFAULT "A1", question INTEGER NOT NULL DEFAULT "1" , original VARCHAR(50), answer VARCHAR(50), `seruid` INTEGER, answer2 VARCHAR(50))
CREATE TABLE `Answers` (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, question INTEGER NOT NULL DEFAULT "0" , profile INTEGER NOT NULL DEFAULT "0", level INTEGER NOT NULL DEFAULT "1" , correct INTEGER NOT NULL, incorrect INTEGER NOT NULL)
CREATE TABLE `Languages` (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name VARCHAR(50), `Icon`  VARCHAR(50))
INSERT INTO 'Languages' VALUES (1,'Английский','eng.png');
INSERT INTO 'Languages' VALUES (2,'Итальянский','ita.png');
INSERT INTO 'Languages' VALUES (3,'Немецкий','deu.png');
INSERT INTO 'Languages' VALUES (4,'Испанский','spa.png');
CREATE TABLE `Preferences` (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `Server_IP`  TEXT, `lastid`  INTEGER) 
CREATE TABLE `Profiles` (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name VARCHAR(50), image VARCHAR(50), lang1 INTEGER, lang2 INTEGER, lang3 INTEGER, `login`  varchar(50), `pass`  varchar(50), `lastdate`  DATETIME, `active`  BOOLEAN, `mon`  boolean, `tue`  INTEGER, `wen`  INTEGER, `tur`  INTEGER, `fri`  INTEGER, `sat`  INTEGER, `sun`  INTEGER, `hour_start`  INTEGER, `hour_end`  INTEGER, `period`  INTEGER, `user_active`  INTEGER, `level1`  INTEGER NOT NULL DEFAULT "1", `level2`  INTEGER NOT NULL DEFAULT "1", `level3`  INTEGER NOT NULL DEFAULT "1")
CREATE TABLE `Questions_type` (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name VARCHAR(50) NOT NULL)
INSERT INTO 'Questions_type' VALUES (1,'Переведите слово');
INSERT INTO 'Questions_type' VALUES (2,'Выберите правильный перевод слова');
INSERT INTO 'Questions_type' VALUES (3,'Выберите правильный перевод выражения');
CREATE TABLE `Tips` (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, lang INTEGER NOT NULL, name TEXT NOT NULL, image VARCHAR(50))
INSERT INTO 'Tips' VALUES (1,1,'Уровень А1. Понимание и умение употреблять в речи знакомые фразы и выражения, необходимые для выполнения конкретных задач. Взможность представиться, задать вопросы о других. Возможность участия в разговоре, если собеседник говорит медленно.','cefr.jpg');
INSERT INTO 'Tips' VALUES (2,1,'Уровень А2. Понимаю отдельные слова и выражения, связанные с основными сферами жизни (например, сведения о себе, членах семьи или друзьях, месте работы, жительства). Обмен информацией на знакомые и бытовые темы.','cefr.jpg');
INSERT INTO 'Tips' VALUES (3,1,'Уровень B1. Понимание основных идей отдельных сообщений, типично возникающих на досуге, на работе, учебе. Умение общаться в большинстве ситуаций. Умение составить связное сообщение на любую тему.','cefr.jpg');
CREATE TABLE 'UserStats (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, lang INTEGER NOT NULL DEFAULT "0", profile INTEGER NOT NULL DEFAULT "0" , days INTEGER DEFAULT "0" , exams INTEGER DEFAULT "0" , exams_complete INTEGER DEFAULT "0" , questions INTEGER DEFAULT "0" , questions_right INTEGER DEFAULT "0");
CREATE TABLE 'Ratings'(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, user TEXT NOT NULL, place INTEGER NOT NULL, rating FLOAT NOT NULL,lang INTEGER NOT NULL);