CREATE TABLE IF NOT EXISTS `tmp_admax_uuid_url` (
  `id`      INT(16)    NOT NULL AUTO_INCREMENT,
  `uuid`    BINARY(16) NOT NULL,
  `width`   INT(5)     NOT NULL,
  `height`  INT(5)     NOT NULL,
  `url`     VARCHAR(2000) DEFAULT NULL,
  `type`    TINYINT(1) NOT NULL,
  `network` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COMMENT ='临时uuid_url映射'
  AUTO_INCREMENT =1

  INSERT INTO tmp_admax_uuid_url(UUID, WIDTH, HEIGHT, URL, TYPE,NETWORK) VALUES (x'390149dab64444ab87cc73e32516e2b4', 468, 60, 'http://static.buzzads.com/js/a/ym.html?zid=144415&w=468&h=60&pid=3&sid=e113378c053a58ad&msid=c62ae18eac016f4c', 1,0);
INSERT INTO tmp_admax_uuid_url (UUID, WIDTH, HEIGHT, URL, TYPE, NETWORK) VALUES (x'fbe7b28e050d4ab39af40740ed16ea11', 610, 100, 'http://static.buzzads.com/js/a/mz.html?id=440', 1, 0);
INSERT INTO tmp_admax_uuid_url (UUID, WIDTH, HEIGHT, URL, TYPE, NETWORK) VALUES (x'553f11d8877f4b7098a8cd30869f9a99', 300, 250, 'http://static.buzzads.com/js/a/as.html?site=ee15d1fe544a86533d49f1a6e9daea39&action=48_2', 1, 0);

