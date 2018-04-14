-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 14, 2018 at 07:24 PM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `splc`
--
CREATE DATABASE IF NOT EXISTS `splc` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `splc`;

-- --------------------------------------------------------

--
-- Table structure for table `privilege`
--

DROP TABLE IF EXISTS `privilege`;
CREATE TABLE IF NOT EXISTS `privilege` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `RoomNumber` text NOT NULL,
  `UserId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `UserId` (`UserId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `privilege`
--

INSERT INTO `privilege` (`Id`, `RoomNumber`, `UserId`) VALUES
(1, '604', 1),
(2, '129', 1),
(3, '903', 1),
(4, '301', 1),
(5, '305', 1),
(6, '100', 2),
(7, '204', 2),
(8, '602', 2),
(9, '500', 3),
(10, '502', 4),
(11, '305', 4),
(12, '102', 4),
(13, '602', 4),
(14, '701', 4),
(15, '101', 5),
(16, '202', 5),
(17, '303', 5),
(18, '404', 5),
(19, '101', 6),
(20, '202', 6),
(21, '303', 6);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Email` text NOT NULL,
  `Password` text NOT NULL,
  `Active` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`Id`, `Email`, `Password`, `Active`) VALUES
(1, 'jankowalski@mail.pl', 'haslo123', 1),
(2, 'nowak@gmail.com', 'now@k321', 1),
(3, 'malgorzata@company.com', 'p@ssw0rd', 1),
(4, 'dawid@wp.pl', '321haslo', 1),
(5, 'lukasz@student.pl', 'haslo123', 1),
(6, 'a', 'b', 1);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `privilege`
--
ALTER TABLE `privilege`
  ADD CONSTRAINT `privilege_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `user` (`Id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
