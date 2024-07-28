-- 학생 데이터 삽입
INSERT INTO student (name, school, major, student_number, grade, mbti, language, interests_korean, interests_english, is_korean, profile_image, create_date, modified_date, matched_at)
VALUES
    ('John Doe', '세종대학교', '컴퓨터공학과', '20201234', 3, 'INTJ', '["English", "Korean"]', '["축구", "등산"]', '["Football", "Hiking"]', false, 'https://blotie.s3.ap-southeast-2.amazonaws.com/profile_image/%ED%94%84%EB%A1%9C%ED%95%841.png', '2024/07/08 14:00:00', '2024/07/08 14:00:00', '2024/07/05 14:00:00');

SET @student_id_1 = LAST_INSERT_ID();

INSERT INTO student (name, school, major, student_number, grade, mbti, language, interests_korean, interests_english, is_korean, profile_image, create_date, modified_date, matched_at)
VALUES
    ('Jane Smith', '세종대학교', '경제학과', '20201235', 2, 'ENFP', '["English", "Japanese"]', '["요리", "음악"]', '["Cooking", "Music"]', false, 'https://blotie.s3.ap-southeast-2.amazonaws.com/profile_image/%ED%94%84%EB%A1%9C%ED%95%842.png', '2024/07/08 14:00:00', '2024/07/08 14:00:00', '2024/07/06 14:00:00');

SET @student_id_2 = LAST_INSERT_ID();

INSERT INTO student (name, school, major, student_number, grade, mbti, language, interests_korean, interests_english, is_korean, profile_image, create_date, modified_date, matched_at)
VALUES
    ('한명수', '세종대학교', '컴퓨터공학과', '20011591', 4, 'ISTP', '["Korean", "English"]', '["게임", "독서"]', '["Gaming", "Reading"]', true, 'https://blotie.s3.ap-southeast-2.amazonaws.com/profile_image/%ED%94%84%EB%A1%9C%ED%95%843.png', '2024/07/08 14:00:00', '2024/07/08 14:00:00', '2024/07/07 14:00:00');

SET @student_id_3 = LAST_INSERT_ID();

INSERT INTO student (name, school, major, student_number, grade, mbti, language, interests_korean, interests_english, is_korean, profile_image, create_date, modified_date, matched_at)
VALUES
    ('김민지', '세종대학교', '데이터사이언스학과', '20201236', 1, 'INFJ', '["Korean", "English"]', '["영화", "음악"]', '["Movies", "Music"]', true, 'https://blotie.s3.ap-southeast-2.amazonaws.com/profile_image/%ED%94%84%EB%A1%9C%ED%95%844.png', '2024/07/08 14:00:00', '2024/07/08 14:00:00', '2024/07/08 14:00:00');

SET @student_id_4 = LAST_INSERT_ID();

INSERT INTO student (name, school, major, student_number, grade, mbti, language, interests_korean, interests_english, is_korean, profile_image, create_date, modified_date, matched_at)
VALUES
    ('이하나', '세종대학교', '데이터사이언스학과', '20201237', 3, 'ENTJ', '["Korean", "Chinese"]', '["수영", "여행"]', '["Swimming", "Traveling"]', true, 'https://blotie.s3.ap-southeast-2.amazonaws.com/profile_image/%ED%94%84%EB%A1%9C%ED%95%845.png', '2024/07/08 14:00:00', '2024/07/08 14:00:00', '2024/07/09 14:00:00');

SET @student_id_5 = LAST_INSERT_ID();


-- 피드 데이터 삽입
INSERT INTO feed (title, content, student_id, comment_count, like_count, create_date, modified_date)
VALUES
    ('🌟 2024년 2학기 창업 동아리 신입 부원 모집 공고 🌟 (Recruitment of start-up club members)', '안녕하세요! 창의적인 아이디어로 세상을 바꾸고 싶은 여러분을 위한 기회가 왔습니다. 2024년 2학기 창업 동아리 신입 부원을 모집합니다. 창업에 열정이 있고, 다양한 배경의 사람들과 협력하여 새로운 아이디어를 현실로 만들고 싶다면 지금 바로 지원하세요! Hello! An opportunity has come for those of you who want to change the world with creative ideas. We are recruiting new members for the startup club for the 2nd semester of 2024. If you have a passion for startups and want to collaborate with people from diverse backgrounds to turn new ideas into reality, please join us! 🎯  지원자격 / Eligibility 창업에 관심 있는 외국인 및 한국인 재학생 (Foreign and Korean students interested in starting a business) 📝 지원 방법 / Application Process Submit Google Form ▶ https://docs.google.com/forms/ 📅 모집 일정 / Recruitment Schedule 지원 기간: 2024년 6월 15일 ~ 7월 31일 Application period: June 15 to July 31, 2024 🚀 동아리 활동 / Club Activities - 정기 회의 및 워크숍 참여 Participation in regular meetings and workshops - 멘토링 프로그램 및 네트워킹 기회 Mentoring program and networking opportunities - 실제 창업 프로젝트 기획 및 실행 Planning and execution of actual startup projects 📞 문의 / Inquiries - E-mail: startup@gmail.com - Facebook: startup.facebook.com 여러분의 혁신적인 아이디어와 열정을 기다립니다! 우리와 함께 세상을 바꾸는 흥미진진한 여정에 참여하세요. 🌈✨ We look forward to your creative ideas and passion! Join us on this exciting journey. 🌈✨', @student_id_1, 3, 3, '2024/07/08 14:00:00', '2024/07/08 14:00:00');

SET @feed_id_1 = LAST_INSERT_ID();

INSERT INTO feed (title, content, student_id, comment_count, like_count, create_date, modified_date)
VALUES
    ('Second Feed Title', 'This is the content of the second feed.', @student_id_2, 2, 2, '2024/07/08 14:00:00', '2024/07/08 14:00:00');

SET @feed_id_2 = LAST_INSERT_ID();

INSERT INTO feed (title, content, student_id, comment_count, like_count, create_date, modified_date)
VALUES
    ('Third Feed Title', 'This is the content of the third feed.', @student_id_3, 0, 0, '2024/07/08 14:00:00', '2024/07/08 14:00:00');

SET @feed_id_3 = LAST_INSERT_ID();

INSERT INTO feed (title, content, student_id, comment_count, like_count, create_date, modified_date)
VALUES
    ('Fourth Feed Title', 'This is the content of the fourth feed.', @student_id_4, 0, 0, '2024/07/08 14:00:00', '2024/07/08 14:00:00');

SET @feed_id_4 = LAST_INSERT_ID();

INSERT INTO feed (title, content, student_id, comment_count, like_count, create_date, modified_date)
VALUES
    ('Fifth Feed Title', 'This is the content of the fifth feed.', @student_id_5, 0, 0, '2024/07/08 14:00:00', '2024/07/08 14:00:00');

SET @feed_id_5 = LAST_INSERT_ID();

-- 댓글 데이터 삽입
INSERT INTO comment (content, student_id, feed_id, comment_number, create_date, modified_date)
VALUES
    ('Im really looking forward to the mentoring program!', @student_id_1, @feed_id_1, 1, '2024/07/08 14:00:00', '2024/07/08 14:00:00'),
('이번 학기에 꼭 참여하고 싶습니다! 좋은 기회 제공해주셔서 감사합니다.', @student_id_2, @feed_id_1, 2, '2024/07/08 14:00:00', '2024/07/08 14:00:00'),
('창업에 대한 열정을 가지고 있는데, 꼭 지원하고 싶어요! 😊', @student_id_3, @feed_id_1, 1, '2024/07/08 14:00:00', '2024/07/08 14:00:00'),
('This is a comment on the third feed.', @student_id_1, @feed_id_3, 1, '2024/07/08 14:00:00', '2024/07/08 14:00:00'),
('Its great that international students can also apply. I want to learn in a global environment!', @student_id_5, @feed_id_2, 1, '2024/07/08 14:00:00', '2024/07/08 14:00:00');

-- 좋아요 데이터 삽입
INSERT INTO user_like (student_id, feed_id)
VALUES
    (@student_id_1, @feed_id_1),
    (@student_id_2, @feed_id_1),
    (@student_id_3, @feed_id_1),
    (@student_id_4, @feed_id_2),
    (@student_id_5, @feed_id_2);

-- 업로드 이미지 데이터 삽입
INSERT INTO upload_image (filename, feed_id)
VALUES
    ('https://blotie.s3.ap-southeast-2.amazonaws.com/feeds/c43ed063-a33b-4cc0-994a-55bb5b19a5cc_%EB%8F%99%EC%95%84%EB%A6%AC_%EB%AA%A8%EC%A7%91_%EC%9D%B4%EB%AF%B8%EC%A7%80.png', @feed_id_1);
