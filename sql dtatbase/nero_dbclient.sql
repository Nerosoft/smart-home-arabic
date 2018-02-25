-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 16, 2018 at 09:15 AM
-- Server version: 10.1.25-MariaDB
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nero_dbclient`
--

-- --------------------------------------------------------

--
-- Table structure for table `arduino_led`
--

CREATE TABLE `arduino_led` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `led` varchar(255) NOT NULL,
  `ledinterval` varchar(255) NOT NULL,
  `refinterval` varchar(255) NOT NULL,
  `statetime` varchar(255) NOT NULL,
  `STL` varchar(255) NOT NULL,
  `pir` tinyint(1) NOT NULL,
  `visibility` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `arduino_led`
--

INSERT INTO `arduino_led` (`id`, `name`, `state`, `led`, `ledinterval`, `refinterval`, `statetime`, `STL`, `pir`, `visibility`) VALUES
(6, 'Living Room7', 'of', '10', '0', '5', 'of', 'of', 0, 1),
(7, 'Living Room8', 'of', '11', '0', '5', 'of', 'of', 0, 1),
(8, 'Living Room9', 'of', '12', '0', '5', 'of', 'of', 0, 1),
(0, 'Living Room1', 'on', '4', '0', '5', 'of', 'of', 0, 1),
(1, 'Living Room2', 'of', '5', '0', '5', 'of', 'of', 0, 1),
(2, 'Living Room3', 'of', '6', '0', '5', 'of', 'of', 0, 1),
(3, 'Living Room4', 'of', '7', '0', '5', 'of', 'of', 0, 1),
(4, 'Living Room5', 'of', '8', '0', '5', 'of', 'of', 0, 1),
(5, 'Living Room6', 'of', '9', '0', '5', 'of', 'of', 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `setting`
--

CREATE TABLE `setting` (
  `id` int(11) NOT NULL,
  `SPIR` tinyint(1) NOT NULL,
  `time` int(5) NOT NULL DEFAULT '5',
  `mode` varchar(5) NOT NULL DEFAULT '2',
  `led` varchar(255) NOT NULL,
  `SIR` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `setting`
--

INSERT INTO `setting` (`id`, `SPIR`, `time`, `mode`, `led`, `SIR`) VALUES
(0, 1, 5, '2', '', 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(9) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL DEFAULT '',
  `GroupU` int(9) NOT NULL DEFAULT '1',
  `comment` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `GroupU`, `comment`) VALUES
(3, 'aaaaaaa', 'aaaaaaa', 1, 'user'),
(29, 'bbbbbbb', '1234567', 1, 'user'),
(1, 'Nero-Soft', 'mmmmmmm', 4, 'admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `arduino_led`
--
ALTER TABLE `arduino_led`
  ADD PRIMARY KEY (`led`);

--
-- Indexes for table `setting`
--
ALTER TABLE `setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`email`),
  ADD KEY `id` (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(9) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
