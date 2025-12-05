-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 04 Des 2025 pada 09.50
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `onex_db`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `history`
--

CREATE TABLE `history` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  `duration` int(11) NOT NULL,
  `difficulty` varchar(20) DEFAULT NULL,
  `played_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `history`
--

INSERT INTO `history` (`id`, `user_id`, `score`, `duration`, `difficulty`, `played_at`) VALUES
(2, 2, 90, 60, 'Easy', '2025-11-25 03:14:40'),
(3, 2, 90, 60, 'Easy', '2025-11-25 03:16:21'),
(4, 2, 120, 34, 'Easy', '2025-11-25 03:17:03'),
(5, 3, 240, 57, 'Medium', '2025-11-26 01:59:43'),
(6, 3, 60, 60, 'Easy', '2025-11-26 08:36:39'),
(7, 3, 120, 22, 'Easy', '2025-11-27 06:23:23'),
(8, 3, 120, 33, 'Easy', '2025-11-27 06:27:22'),
(9, 3, 20, 60, 'Easy', '2025-11-27 08:28:56'),
(10, 3, 120, 46, 'Easy', '2025-11-27 08:36:27'),
(11, 3, 120, 17, 'Easy', '2025-11-27 08:50:37'),
(12, 3, 120, 47, 'Easy', '2025-11-27 08:59:58'),
(13, 3, 20, 60, 'Easy', '2025-11-27 09:48:57'),
(14, 3, 120, 38, 'Easy', '2025-11-27 10:05:10'),
(15, 3, 50, 60, 'Easy', '2025-11-27 10:16:15'),
(16, 3, 10, 60, 'Easy', '2025-11-27 10:17:19'),
(17, 3, 140, 55, 'Easy', '2025-12-01 11:08:31'),
(18, 3, 120, 36, 'Easy', '2025-12-01 11:18:43'),
(19, 3, 250, 61, 'Medium', '2025-12-03 00:39:32'),
(20, 3, 120, 13, 'Easy', '2025-12-03 00:55:19'),
(21, 3, 0, 120, 'Medium', '2025-12-03 01:01:11'),
(22, 3, 120, 22, 'Easy', '2025-12-03 01:31:06'),
(23, 3, 240, 90, 'Medium', '2025-12-03 01:32:44'),
(24, 3, 480, 171, 'Hard', '2025-12-04 01:29:52'),
(25, 3, 120, 50, 'Easy', '2025-12-04 08:18:17');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `created_at`) VALUES
(2, 'ridho', '123', '2025-11-25 03:13:19'),
(3, 'spica', '123', '2025-11-26 01:58:39');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `history`
--
ALTER TABLE `history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
