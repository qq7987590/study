-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.6.11 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 finalwork.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT '0',
  `type` int(11) DEFAULT '0',
  `name` varchar(50) DEFAULT '0',
  `password` varchar(50) DEFAULT '0',
  `phone` varchar(50) DEFAULT '0',
  `sex` varchar(50) DEFAULT '0',
  `birthday` varchar(50) DEFAULT '0',
  `idcard` varchar(50) DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  finalwork.user 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`uid`, `email`, `type`, `name`, `password`, `phone`, `sex`, `birthday`, `idcard`) VALUES
	(1, 'qq7987590', 0, '李柄坤', 'qq7916252', '0', '0', '0', '0'),
	(2, 'q', 1, '0', 'q', '0', '0', '0', '0');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
