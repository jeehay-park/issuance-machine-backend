
-- machine : 발급기 정보 테이블
CREATE TABLE IF NOT EXISTS `machine` (
  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',
  `mcn_id` varchar(32) NOT NULL COMMENT '발급기 고유 ID. mcn_ + seq의 형식',
  `mcn_name` varchar(64) DEFAULT NULL COMMENT '발급기 이름',
  `etc` text DEFAULT NULL COMMENT '기타 정보',
  `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간',
  `created_at` datetime NOT NULL COMMENT '등록 시간',
  `comment` text DEFAULT NULL COMMENT '주석 기타정보',
  PRIMARY KEY (`mcn_id`),
  UNIQUE KEY `IDX_MACHINE_01_UK` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


-- machine_device : 발급기의 디바이스 테이블
CREATE TABLE IF NOT EXISTS `machine_device` (
  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',
  `dvc_id` varchar(32) NOT NULL COMMENT '발급기 디바이스 고유 ID. dvc_ + seq의 형식',
  `dvc_name` varchar(64) DEFAULT NULL COMMENT '발급기 디바이스 이름',
  `dvc_num` int(8) NOT NULL COMMENT '디바이스 번호. 발급기 기준으로 1번부터 할당',
  `mcn_id` varchar(32) NOT NULL COMMENT '발급기 고유 ID. mcn_ + seq의 형식',
  `ip` varchar(32) DEFAULT NULL COMMENT '디바이스 IP 주소',
  `rom_ver` varchar(32) DEFAULT NULL COMMENT '디바이스 롬(rom) 버전',
  `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간',
  `created_at` datetime NOT NULL COMMENT '등록 시간',
  `comment` text DEFAULT NULL COMMENT '주석 기타정보',
  PRIMARY KEY (`dvc_id`),
  UNIQUE KEY `IDX_MACHINE_DEVICE_01_UK` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


-- sn_rule : 시리얼넘버 규칙(Rule) 테이블
CREATE TABLE IF NOT EXISTS `sn_rule` (
  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',
  `snr_id` varchar(32) NOT NULL COMMENT 'SN 규칙 ID. snr_ + seq의 형식',
  `snr_name` varchar(64) DEFAULT NULL COMMENT 'SN 규칙 이름 (대상 제품)',
  `test_code` varchar(64) DEFAULT NULL COMMENT '테스트 코드',
  `location` int(8) DEFAULT NULL COMMENT '작업 위치',
  `last_burn_date` datetime DEFAULT NULL COMMENT '최근 Burn 시간',
  `today_count` int(8) DEFAULT NULL COMMENT '금일 건수',
  `count_sum` int(8) DEFAULT NULL COMMENT '건수 합계',
  `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간',
  `created_at` datetime NOT NULL COMMENT '등록 시간',
  `comment` text DEFAULT NULL COMMENT '주석 기타정보',
  PRIMARY KEY (`snr_id`),
  UNIQUE KEY `IDX_SN_RULE_01_UK` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


-- code_info : 발급장비 코드(code) 정보 테이블
CREATE TABLE IF NOT EXISTS `code_info` (
  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',
  `code_id` varchar(32) NOT NULL COMMENT '코드 ID. cd_ + seq의 형식',
  `code_name` varchar(64) NOT NULL COMMENT '코드 이름',
  `code_group` varchar(32) DEFAULT NULL COMMENT '코드 그룹(필요시)',
  `description` varchar(256) DEFAULT NULL COMMENT '코드에 대한 상세 설명',
  `status` varchar(32) NOT NULL COMMENT '코드 상태. USE/NOTUSE/DELETED',
  `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간',
  `created_at` datetime NOT NULL COMMENT '등록 시간',
  `comment` text DEFAULT NULL COMMENT '주석 기타정보',
  PRIMARY KEY (`code_id`),
  UNIQUE KEY `IDX_CODE_INFO_01_UK` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


-- code_enum : 발급장비 코드(code) ENUM값 상세 테이블
CREATE TABLE IF NOT EXISTS `code_enum` (
  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',
  `enum_seq` int(8) NOT NULL COMMENT '코드에 따른 ENUM의 순번',
  `enum_id` varchar(32) NOT NULL COMMENT '코드별 ENUM ID. code_id + _ + enum_seq 의 형식 ',
  `code_id` varchar(32) NOT NULL COMMENT '코드 ID. cd_ + seq의 형식',
  `enum_value` varchar(64) NOT NULL COMMENT '코드별 ENUM 값',
  `is_mandatory` varchar(16) DEFAULT 'Y' COMMENT '필수여부 Y, N',
  `order` int(8) NOT NULL COMMENT '코드별 ENUM 표시 순서. 1부터 시작',
  `description` varchar(256) DEFAULT NULL COMMENT '코드 ENUM에 대한 상세설명',
  PRIMARY KEY (`enum_id`),
  UNIQUE KEY `IDX_CODE_ENUM_01_UK` (`seq`),
  KEY `IDX_CODE_ENUM_01` (`code_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


-- profile_config : 발급 프로파일 설정 데이터 테이블
CREATE TABLE IF NOT EXISTS `profile_config` (
  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',
  `prof_id` varchar(32) NOT NULL COMMENT '프로파일 ID. prof_ + seq 의 형식',
  `prof_name` varchar(256) NOT NULL COMMENT '프로파일 이름',
  `description` varchar(256) DEFAULT NULL COMMENT '프로파일 상세 설명',
  `prof_type` varchar(32) DEFAULT NULL COMMENT '프로파일 타입(필요시)',
  `version` varchar(32) DEFAULT NULL COMMENT '버전 ex: 2.05',
  `ctnt_data` text NOT NULL COMMENT '프로파일 컨텐츠 데이터',
  `data_hash` varchar(64) DEFAULT NULL COMMENT '프로파일 데이터 해시',
  `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간',
  `created_at` datetime NOT NULL COMMENT '등록 시간',
  `comment` text DEFAULT NULL COMMENT '주석 기타정보',
  PRIMARY KEY (`prof_id`),
  UNIQUE KEY `IDX_PROFILE_CONFIG_01_UK` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


-- keyissue_config : 키발급 코드 설정 데이터 테이블
CREATE TABLE IF NOT EXISTS `keyissue_config` (
  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',
  `keyis_id` varchar(32) NOT NULL COMMENT '키발급코드 ID. kis_ + seq 의 형식',
  `keyis_name` varchar(256) NOT NULL COMMENT '키발급코드 이름',
  `description` varchar(256) DEFAULT NULL COMMENT '키발급코드 상세 설명',
  `keyis_type` varchar(32) DEFAULT NULL COMMENT '키발급코드 타입(필요시)',
  `version` varchar(32) DEFAULT NULL COMMENT '버전 ex: 2.05',
  `ctnt_data` text NOT NULL COMMENT '프로파일 컨텐츠 데이터',
  `data_hash` varchar(64) DEFAULT NULL COMMENT '프로파일 데이터 해시',
  `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간',
  `created_at` datetime NOT NULL COMMENT '등록 시간',
  `comment` text DEFAULT NULL COMMENT '주석 기타정보',
  PRIMARY KEY (`keyis_id`),
  UNIQUE KEY `IDX_KEYISSUE_CONFIG_01_UK` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


-- script_config : 키발급 코드 설정 데이터 테이블
CREATE TABLE IF NOT EXISTS `script_config` (
  `seq` int(11) NOT NULL COMMENT '순번 1부터 시작',
  `scrt_id` varchar(32) NOT NULL COMMENT '스크립트 ID. scrt_ + seq 의 형식',
  `scrt_name` varchar(256) NOT NULL COMMENT '스크립트 이름',
  `description` varchar(256) DEFAULT NULL COMMENT '스크립트 상세 설명',
  `scrt_type` varchar(32) DEFAULT NULL COMMENT '스크립트 타입(필요시)',
  `version` varchar(32) DEFAULT NULL COMMENT '버전 ex: 2.05',
  `ctnt_data` text NOT NULL COMMENT '프로파일 컨텐츠 데이터',
  `data_hash` varchar(64) DEFAULT NULL COMMENT '프로파일 데이터 해시',
  `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간',
  `created_at` datetime NOT NULL COMMENT '등록 시간',
  `comment` text DEFAULT NULL COMMENT '주석 기타정보',
  PRIMARY KEY (`scrt_id`),
  UNIQUE KEY `IDX_SCRIPT_CONFIG_01_UK` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


-- program_info : 프로그램 정보 테이블 (기존 프로젝트 테이블)
CREATE TABLE IF NOT EXISTS `program_info` (
  `seq` int(11) NOT NULL COMMENT '순번. 1부터 시작',
  `prog_id` varchar(32) NOT NULL COMMENT '발급 프로그램 고유 ID. prog_ + seq 의 형식',
  `prog_name` varchar(64) DEFAULT NULL COMMENT '발급 프로그램 이름',
  `product` varchar(32) DEFAULT NULL COMMENT '제품 (칩 Chip)',
  `test_code` varchar(64) DEFAULT NULL COMMENT '테스트 코드',
  `description` text DEFAULT NULL COMMENT '스크립트 상세 설명',
  `status` varchar(32) NOT NULL COMMENT '프로그램 상태 ACTIVE/NOTUSE/DELETED',
  `param` varchar(128) DEFAULT NULL COMMENT '파라미터',
  `param_ext` mediumtext DEFAULT NULL COMMENT '파라미터 확장',
  `is_encrypted_sn` varchar(16) DEFAULT NULL COMMENT 'SN 인크립션 여부',
  `prof_id` varchar(32) DEFAULT NULL COMMENT '프로파일 ID. prof_ + seq 의 형식',
  `keyis_id` varchar(32) DEFAULT NULL COMMENT '키발급코드 ID. kis_ + seq 의 형식',
  `scrt_id` varchar(32) DEFAULT NULL COMMENT '스크립트 ID. scrt_ + seq 의 형식',
  `session_handler` varchar(128) DEFAULT NULL COMMENT '발급 핸들러 이름 (클래스 코드로 관리)',
  `etc_option` varchar(64) DEFAULT NULL COMMENT '기타 코드 옵션 명. 여러개인 경우 파이프(|)로 구분',
  `company_code` varchar(32) DEFAULT NULL COMMENT '회사코드. ex GLOBAL LGU HAIER FINGER_CHIP LG_INNOTEK LGE 등',
  `country_code` varchar(32) DEFAULT NULL COMMENT '국가코드. ex : GLOBAL',
  `interface_type` varchar(32) DEFAULT NULL COMMENT '인터페이스 타입. ex : I2C SPI ONE_WIRE e7816',
  `package_type` varchar(32) DEFAULT NULL COMMENT '패키지 타입. ex : SOIC uDFN SOT_23 e7816',
  `updated_at` datetime DEFAULT NULL COMMENT '업데이트 시간',
  `created_at` datetime NOT NULL COMMENT '등록 시간',
  `comment` text DEFAULT NULL COMMENT '주석 기타정보',
  PRIMARY KEY (`prog_id`),
  UNIQUE KEY `IDX_PROGRAM_INFO_01_UK` (`seq`),
  KEY `IDX_PROGRAM_INFO_01` (`product`),
  KEY `IDX_PROGRAM_INFO_02` (`prof_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;


-- work_info : 발급 작업 정보 테이블
CREATE TABLE IF NOT EXISTS `work_info` (
  `seq` int(11) NOT NULL COMMENT '순번. 1부터 시작',
  `work_id` varchar(32) NOT NULL COMMENT '발급작업 ID. wrk_ + seq 의 형식',
  `work_no` varchar(128) DEFAULT NULL COMMENT '발급 작업 표시 넘버',
  `tag_name` varchar(128) DEFAULT NULL COMMENT '태그 이름',
  `customer` varchar(64) DEFAULT NULL COMMENT '고객',
  `device_name` varchar(32) DEFAULT NULL COMMENT '디바이스 이름',
  `order_no` varchar(32) DEFAULT NULL COMMENT '오더 넘버',
  `prog_id` varchar(32) NOT NULL COMMENT '발급 프로그램 고유 ID. prog_ + seq 의 형식',
  `prog_name` varchar(64) DEFAULT NULL COMMENT '발급 프로그램 이름',
  `mcn_id` varchar(32) NOT NULL COMMENT '발급기 고유 ID. mcn_ + seq의 형식',
  `snr_id` varchar(32) DEFAULT NULL COMMENT 'SN 규칙 ID. snr_ + seq의 형식',
  `target_size` int(11) DEFAULT 0 COMMENT '발급 목표수량',
  `completed_size` int(11) DEFAULT 0 COMMENT '발급 진행(완료)수량',
  `failed_size` int(11) DEFAULT 0 COMMENT '발급 실패(오류)수량',
  `check_size` int(11) DEFAULT 0 COMMENT '발급 검증수량',
  `due_date` datetime DEFAULT NULL COMMENT '작업완료 예정 시간',
  `description` text DEFAULT NULL COMMENT '작업 상세 설명',
  `is_lock` varchar(16) NOT NULL COMMENT '발급칩의 LOCK 여부',
  `status` varchar(32) NOT NULL COMMENT '작업 상태 INIT/READY/RUNNING/ON_STOP/FINISHED',
  `param` varchar(128) DEFAULT NULL COMMENT '파라미터',
  `param_ext` mediumtext DEFAULT NULL COMMENT '파라미터 확장',
  `detail_msg` text DEFAULT NULL COMMENT '작업 상세 메시지',
  `started_at` datetime DEFAULT NULL COMMENT '작업 시작 시간',
  `updated_at` datetime DEFAULT NULL COMMENT '작업 업데이트 시간',
  `created_at` datetime NOT NULL COMMENT '작업 등록 시간',
  `comment` text DEFAULT NULL COMMENT '주석 기타정보',
  PRIMARY KEY (`work_id`),
  UNIQUE KEY `IDX_WORK_INFO_01_UK` (`seq`),
  KEY `IDX_WORK_INFO_01` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

















