## 05 Mock 객체 Stubbing 연습 문제
####다음 코드의 // TODO에 해당하는 작업을 코딩으로 채워 넣으세요.

```
Study study = new Study(10, "테스트");

// TODO memberService 객체에 findById 메소드를 1L 값으로 호출하면 Optional.of(member) 객체를 리턴하도록 Stubbing
// TODO studyRepository 객체에 save 메소드를 study 객체로 호출하면 study 객체 그대로 리턴하도록 Stubbing

studyService.createNewStudy(1L, study);

assertNotNull(study.getOwner());
assertEquals(member, study.getOwner());
```
git checkout 216112f5706fef76f56c735faed200f311b8d919
