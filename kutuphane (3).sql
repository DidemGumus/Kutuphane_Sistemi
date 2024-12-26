-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 24 Ara 2024, 22:56:27
-- Sunucu sürümü: 10.4.32-MariaDB
-- PHP Sürümü: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `kutuphane`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `books`
--

CREATE TABLE `books` (
  `book_id` int(11) NOT NULL,
  `book_name` varchar(200) NOT NULL,
  `author` varchar(100) NOT NULL,
  `genre` varchar(50) DEFAULT NULL,
  `publication_year` int(11) DEFAULT NULL,
  `status` enum('Available','Borrowed','Reserved','Lost') DEFAULT 'Available',
  `location` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `books`
--

INSERT INTO `books` (`book_id`, `book_name`, `author`, `genre`, `publication_year`, `status`, `location`) VALUES
(22, 'b', 'b', 'b', 2005, 'Lost', 'd'),
(23, 'sdcs', 'sss', 'dsd', 2001, 'Borrowed', 'cdcd'),
(24, 'ded', 'ddd', 'ddd', 2003, 'Borrowed', 'dededed');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `booksearchindex`
--

CREATE TABLE `booksearchindex` (
  `search_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `search_keywords` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `booksearchindex`
--

INSERT INTO `booksearchindex` (`search_id`, `book_id`, `search_keywords`) VALUES
(14, 22, 'b b b'),
(15, 23, 'sdcs sss dsd'),
(16, 24, 'ded ddd ddd');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `logs`
--

CREATE TABLE `logs` (
  `log_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `action` varchar(255) NOT NULL,
  `timestamp` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `rolespermissions`
--

CREATE TABLE `rolespermissions` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(50) NOT NULL,
  `permissions` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `borrow_date` datetime DEFAULT current_timestamp(),
  `return_date` datetime DEFAULT NULL,
  `status` enum('Borrowed','Returned','Late') DEFAULT 'Borrowed'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `book_id`, `user_id`, `borrow_date`, `return_date`, `status`) VALUES
(52, 23, 1, '2024-12-19 19:40:42', NULL, 'Returned'),
(53, 22, 1, '2024-12-19 19:46:11', NULL, 'Returned'),
(54, 24, 1, '2024-12-19 19:58:41', NULL, 'Borrowed'),
(55, 22, 1, '2024-12-19 19:58:54', NULL, 'Borrowed'),
(56, 23, 1, '2024-12-19 20:18:07', NULL, 'Borrowed');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_type` enum('Admin','Personnel','Student') NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `registration_date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `user_type`, `email`, `registration_date`) VALUES
(1, 'didi', '123', 'Student', 'ad@gmail.com', '2024-12-05 21:12:45'),
(2, 'kadir', '123', 'Personnel', 'kadir@gmail.com', '2024-12-07 16:54:30'),
(3, '', '', 'Student', '', '2024-12-14 17:11:43'),
(8, 'işlk', '12345', 'Personnel', '12345', '2024-12-14 17:25:13'),
(11, 'ghjj', '1212', 'Personnel', 'vhgvh@gmail.com', '2024-12-14 17:26:19'),
(12, 'tghth', '1212', 'Personnel', 'ethth@gmail.com', '2024-12-14 17:42:41'),
(15, 'zeycan', '2525', 'Personnel', 'zeycan@gmail.com', '2024-12-15 01:03:45'),
(16, 'sami', '123', 'Student', 'sami@gmail.com', '2024-12-15 01:57:15'),
(17, 'asd', 'asd', 'Personnel', 'asd@gmail.com', '2024-12-15 02:34:39'),
(19, 'kadir17', 'kadir', 'Student', 'kadir17@gmail.com', '2024-12-17 20:55:41'),
(20, 'semra', 'semra', 'Student', 'semra@gmail.com', '2024-12-17 22:28:55'),
(21, 'mehmet', '123', 'Personnel', 'mehmet@gmail.com', '2024-12-19 19:56:45');

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`book_id`);

--
-- Tablo için indeksler `booksearchindex`
--
ALTER TABLE `booksearchindex`
  ADD PRIMARY KEY (`search_id`),
  ADD KEY `book_id` (`book_id`);

--
-- Tablo için indeksler `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Tablo için indeksler `rolespermissions`
--
ALTER TABLE `rolespermissions`
  ADD PRIMARY KEY (`role_id`);

--
-- Tablo için indeksler `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `book_id` (`book_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Tablo için indeksler `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `books`
--
ALTER TABLE `books`
  MODIFY `book_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Tablo için AUTO_INCREMENT değeri `booksearchindex`
--
ALTER TABLE `booksearchindex`
  MODIFY `search_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Tablo için AUTO_INCREMENT değeri `logs`
--
ALTER TABLE `logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `rolespermissions`
--
ALTER TABLE `rolespermissions`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- Tablo için AUTO_INCREMENT değeri `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `booksearchindex`
--
ALTER TABLE `booksearchindex`
  ADD CONSTRAINT `booksearchindex_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `logs`
--
ALTER TABLE `logs`
  ADD CONSTRAINT `logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL;

--
-- Tablo kısıtlamaları `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
