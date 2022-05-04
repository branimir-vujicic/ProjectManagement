-- MySQL dump 10.13  Distrib 8.0.27, for Linux (x86_64)
--
-- Host: localhost    Database: pm
-- ------------------------------------------------------
-- Server version	8.0.27-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS pm;
USE pm;


--
-- Table structure for table `app_settings`
--

DROP TABLE IF EXISTS `app_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_settings`
(
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `created_by`      varchar(255) DEFAULT NULL,
    `created_on`      datetime(6)  DEFAULT NULL,
    `deleted`         int    NOT NULL,
    `modified_by`     varchar(255) DEFAULT NULL,
    `updated_on`      datetime(6)  DEFAULT NULL,
    `version`         bigint       DEFAULT NULL,
    `notifications`   int    NOT NULL,
    `organization`    varchar(255) DEFAULT NULL,
    `setup_completed` int    NOT NULL,
    `text_size`       varchar(16)  DEFAULT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `app_settings`
--

LOCK TABLES `app_settings` WRITE;
/*!40000 ALTER TABLE `app_settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `created_by`  varchar(255) DEFAULT NULL,
    `created_on`  datetime(6)  DEFAULT NULL,
    `deleted`     int    NOT NULL,
    `modified_by` varchar(255) DEFAULT NULL,
    `updated_on`  datetime(6)  DEFAULT NULL,
    `version`     bigint       DEFAULT NULL,
    `text`        longtext,
    `time`        datetime(6)  DEFAULT NULL,
    `title`       varchar(255) DEFAULT NULL,
    `parent_id`   bigint       DEFAULT NULL,
    `project_id`  bigint NOT NULL,
    `user_id`     bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKde3rfu96lep00br5ov0mdieyt` (`parent_id`),
    KEY `FKb5kenf6fjka6ck0snroeb5tmh` (`project_id`),
    KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
    CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FKb5kenf6fjka6ck0snroeb5tmh` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
    CONSTRAINT `FKde3rfu96lep00br5ov0mdieyt` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`)
) DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (10000);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `modified_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `date` date DEFAULT NULL,
  `description` text,
  `time_from` time DEFAULT NULL,
  `time_to` time DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO project (id, created_by, created_on, deleted, modified_by, updated_on, version, archived, date, description, time_from, time_to, title) VALUES (2, 'bvujicic@gmail.com', '2022-04-08 10:03:32.923874', 0, 'bvujicic@gmail.com', '2022-04-08 10:03:47.012483', 4, 0, '2022-04-22', 'test project', '10:30:00', '11:00:00', 'test project development');
INSERT INTO project (id, created_by, created_on, deleted, modified_by, updated_on, version, archived, date, description, time_from, time_to, title) VALUES (3, 'bvujicic@gmail.com', '2022-04-08 10:03:32.923874', 1, 'bvujicic@gmail.com', '2022-04-08 10:03:47.012483', 4, 0, '2022-04-22', 'test development project', '10:30:00', '11:00:00', 'test development project');
INSERT INTO project (id, created_by, created_on, deleted, modified_by, updated_on, version, archived, date, description, time_from, time_to, title) VALUES (4, 'bvujicic@gmail.com', '2022-04-08 10:03:32.923874', 0, 'bvujicic@gmail.com', '2022-04-08 10:03:47.012483', 4, 0, '2022-04-22', 'test research project', '10:30:00', '11:00:00', 'test research project');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Table structure for table `project_tags`
--

DROP TABLE IF EXISTS `project_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_tags` (
  `project_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  PRIMARY KEY (`project_id`,`tag_id`),
  KEY `FKpev86t1kt79tiwevyfpyi9yii` (`tag_id`),
  CONSTRAINT `FKfvy64usu7e9x7ev6obh91q0qe` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `FKpev86t1kt79tiwevyfpyi9yii` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_tags`
--

LOCK TABLES `project_tags` WRITE;
/*!40000 ALTER TABLE `project_tags` DISABLE KEYS */;
INSERT INTO `project_tags` VALUES (44,4),(49,4),(49,5);
/*!40000 ALTER TABLE `project_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `modified_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (4,'bvujicic@gmail.com','2021-10-31 23:39:11.915670',0,'bvujicic@gmail.com','2021-10-31 23:39:33.490513',1,'red','Research and Development'),(5,'bvujicic@gmail.com','2021-10-31 23:39:17.466836',0,'bvujicic@gmail.com','2021-10-31 23:39:17.466856',0,'green','Meeting');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  `modified_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `alphabet` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `email_verification_key` varchar(255) DEFAULT NULL,
  `last_login_time` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `organization` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `password_recovery_key` varchar(255) DEFAULT NULL,
  `password_recovery_time` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_idx` (`email`)
) DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO user (id, created_by, created_on, deleted, modified_by, updated_on, version, active, email, email_verification_key, last_login_time, name, organization, password, password_recovery_key, password_recovery_time) VALUES (1, null, null, false, 'bvujicic@gmail.com', '2022-04-11 12:48:16.377516', 8, true, 'bvujicic@gmail.com', null, null, 'Banimir Vujičić', null, '$2a$10$/3cvXs2pPgzk2Fjmt2/mdOMXK6nQV5dsDO1sdIAcm62HL6QIccZum', null, null);
INSERT INTO user (id, created_by, created_on, deleted, modified_by, updated_on, version, active, email, email_verification_key, last_login_time, name, organization, password, password_recovery_key, password_recovery_time) VALUES (10011, null, null, false, null, null, 0, true, 'petar.petrovic@eprotokol.com', null, null, 'Petar Petrović', null, '$2a$10$e24CwqC4AJWsuVHYlfMjeeyFm/OgKNRUpm/0xTHws77zNsrIC889q', null, null);


/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  KEY `FK55itppkw3i07do3h7qoclqd4k` (`user_id`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,'ADMIN'),(1,'USER'),(10011,'USER');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'eprotokol'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-01  6:24:09
