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

-- 导出  表 finalwork.report 结构
CREATE TABLE IF NOT EXISTS `report` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `distributor` varchar(50) DEFAULT NULL,
  `saleman` varchar(50) DEFAULT NULL,
  `assessment` varchar(50) DEFAULT NULL,
  `treasuter` varchar(50) DEFAULT NULL,
  `clerk` varchar(50) DEFAULT NULL,
  `fist_appraiser` varchar(50) DEFAULT NULL,
  `second_appraiser` varchar(50) DEFAULT NULL,
  `first_assess_number` varchar(50) DEFAULT NULL,
  `report_month` varchar(50) DEFAULT NULL,
  `contacts` varchar(50) DEFAULT NULL,
  `contacts_phone` varchar(50) DEFAULT NULL,
  `first_distributor` varchar(50) DEFAULT NULL,
  `second_distributor` varchar(50) DEFAULT NULL,
  `third_distributor` varchar(50) DEFAULT NULL,
  `street` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `assessment_remark` varchar(50) DEFAULT NULL,
  `village_name` varchar(50) DEFAULT NULL,
  `assess_date` date DEFAULT NULL,
  `outside_time` int(11) DEFAULT NULL,
  `assess_price` float DEFAULT NULL,
  `fist_appraiser_remark` varchar(50) DEFAULT NULL,
  `second_appraiser_remark` varchar(50) DEFAULT NULL,
  `report_number` varchar(50) DEFAULT NULL,
  `report_date` date DEFAULT NULL,
  `report_type` varchar(50) DEFAULT NULL,
  `clerk_remark` varchar(50) DEFAULT NULL,
  `fee` float DEFAULT NULL,
  `treasuter_remark` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_uid` int(11) DEFAULT NULL,
  `stat` int(11) DEFAULT '0',
  PRIMARY KEY (`rid`),
  UNIQUE KEY `report_number` (`report_number`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- 正在导出表  finalwork.report 的数据：~6 rows (大约)
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` (`rid`, `distributor`, `saleman`, `assessment`, `treasuter`, `clerk`, `fist_appraiser`, `second_appraiser`, `first_assess_number`, `report_month`, `contacts`, `contacts_phone`, `first_distributor`, `second_distributor`, `third_distributor`, `street`, `location`, `assessment_remark`, `village_name`, `assess_date`, `outside_time`, `assess_price`, `fist_appraiser_remark`, `second_appraiser_remark`, `report_number`, `report_date`, `report_type`, `clerk_remark`, `fee`, `treasuter_remark`, `create_time`, `last_time`, `last_uid`, `stat`) VALUES
	(14, 'wer', '3123', 'lbkk2', 'wer', 'wer', 'q', 'e', 'werjj', 'wer', 'wer', 'wer', 'wer', 'wer', 'wer', 'wer', 'wer', 'wer', 'wer', '0000-00-01', 18, 18.1, 'qwe', 'rewq', 'er', '0000-00-01', '商贷', 'fdfewa', 15.5, 'qwewer', '2015-06-22 21:52:55', '2015-06-24 16:57:05', 3, 0),
	(17, '0', '3123', '0', NULL, NULL, '0', '0', '0', '', '', '', '', '', '', 'hgxvbb', 'ryhgcvv', '', 'rffff', '0000-00-00', 0, 0, '', '', 'etggdyu', '0000-00-00', '0', '', 0, '', '2015-06-22 21:54:53', '2015-06-22 23:12:18', 3, 1),
	(19, '0', '3123', '0', NULL, NULL, '0', '0', '0', '', '', '', '', '', '', 'hgxvbb', 'ryhgcvv', '', 'rffff', '0000-00-00', 0, 0, '', '', 'etggdyuer', '0000-00-00', '0', '', 0, '', '2015-06-22 21:54:53', '2015-06-22 23:12:19', 3, 2),
	(20, '0', '3123', '0', NULL, NULL, 'q', 'q', '0', '', '', '', '', '', '', 'hgxvbb', 'ryhgcvv', '', 'rffff', '0000-00-00', 0, 0, '', '', '22', '0000-00-00', '0', '', 0, '', '2015-06-22 21:54:53', '2015-06-24 17:02:11', 3, 2),
	(21, 'hhhtl', 'lbkk1', 'lbkk2', NULL, NULL, 'q', 'sss', '', '', '', '', '', '', 'fhjii', '', '', '', '', '0000-00-00', 0, 0, '', '', 'dgjdsa nn', '0000-00-00', '商贷', '', 0, '', '2015-06-23 20:27:33', '2015-06-24 18:16:03', 3, 1),
	(22, 'hhhtl', '3123', 'lbkk2', NULL, NULL, 'q', 'sss', '', '', '', '', '', '', 'fhjii', '', '', '', '', '0000-00-00', 0, 0, '', '', 'dgjdsa nnyhj', '0000-00-00', '0', '', 0, '', '2015-06-23 20:28:12', NULL, 3, 0);
/*!40000 ALTER TABLE `report` ENABLE KEYS */;


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
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- 正在导出表  finalwork.user 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`uid`, `email`, `type`, `name`, `password`, `phone`, `sex`, `birthday`, `idcard`) VALUES
	(2, 'q', 3, '3123', 'dddf', 'llltlkkrrrrrrr', 'm', 'jjh', 'ddvg'),
	(3, 'a', 7, 'hhhtl', 'a', '188988381t', 'f', '1993-08-01t', '231084199308013732t'),
	(4, 'qq7987590', 6, 'sss', 'qq7916252', '0', 'f', '0', '0'),
	(5, 'qwe', 5, 'q', 'q', 'q', 'm', 'fsae', 'wer'),
	(6, 'r', 6, 'e', 'g', 'r', 'f', 'd', '0'),
	(9, '12321', 1, 'lbkk1', 'dddf', 'lll', 'm', 'jjh', 'ddvg'),
	(10, '123121', 2, 'lbkk2', 'dddf', 'lll', 'm', 'jjh', 'ddvg'),
	(11, '', 0, '', '', '', '', '', '0'),
	(12, 'rhjh', 0, 'ejj', '', 'vxsf', 'm', 'sfgh', '0'),
	(15, 'rhjhsgg', 0, 'ejjtedfg', 'hg', 'vxsf', 'm', 'sfgh', '0'),
	(16, 'ffss', 0, 'hgfdss', 'ss', 'rfh', 'm', 'dgg', '0'),
	(17, 'fgf', 0, 'erfd', 'fjk', ' gffdd', 'f', 'dff', '0'),
	(18, 'dgju', 2, 'lvk', 'fhh', 'tf', 'f', 'afcg', 'eaa');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
