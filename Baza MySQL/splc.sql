-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 02, 2018 at 03:38 PM
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
CREATE TABLE `privilege` (
  `Id` int(11) NOT NULL,
  `RoomNumber` text NOT NULL,
  `UserId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
CREATE TABLE `user` (
  `Id` int(11) NOT NULL,
  `Email` text NOT NULL,
  `Password` text NOT NULL,
  `IsAdmin` tinyint(1) NOT NULL,
  `Active` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`Id`, `Email`, `Password`, `IsAdmin`, `Active`) VALUES
(1, 'admin@polsl.pl', 'aei', 1, 1),
(2, 'nowak@gmail.com', 'now@k321', 0, 1),
(3, 'malgorzata@company.com', 'p@ssw0rd', 0, 1),
(4, 'dawid@wp.pl', '321haslo', 0, 1),
(5, 'lukasz@student.pl', 'haslo123', 0, 1),
(6, 'jankowalski@mail.pl', 'haslo123', 0, 1);


--
-- Indexes for dumped tables
--

--
-- Indexes for table `privilege`
--
ALTER TABLE `privilege`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `UserId` (`UserId`) USING BTREE;

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `privilege`
--
ALTER TABLE `privilege`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `privilege`
--
ALTER TABLE `privilege`
  ADD CONSTRAINT `privilege_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `user` (`Id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
