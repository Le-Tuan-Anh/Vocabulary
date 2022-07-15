javafx-sdk-18.0.1
jdk-18.0.1.1 2022-04-22

database mysql:
    DB_NAME = "english";
    USER_NAME = "root";
    PASSWORD = "admin";
    tables:
        - words:
            word_ID	int	not null	PRI		auto_increment
            word_target	tinytext	not null			
            word_explain	tinytext	not null			
            word_phonetic	tinytext	not null			
            mark	tinyint(1)	not null		0	
        - examples:
            example_ID	int	not null	PRI		auto_increment
            word_ID	int	not null			
            englishEx	text	not null			
            vietEx	text	not null			