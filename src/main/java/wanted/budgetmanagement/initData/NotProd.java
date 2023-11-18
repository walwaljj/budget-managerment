package wanted.budgetmanagement.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import wanted.budgetmanagement.config.security.PasswordEncoderConfig;
import wanted.budgetmanagement.domain.Category;
import wanted.budgetmanagement.domain.Expenditure;
import wanted.budgetmanagement.domain.user.entity.User;
import wanted.budgetmanagement.repository.ExpenditureRepository;
import wanted.budgetmanagement.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Configuration
@Profile({"dev", "test"})
public class NotProd {
	@Bean
	CommandLineRunner initData(UserRepository memberRepository,
							   ExpenditureRepository expenditureRepository) {

		PasswordEncoderConfig passwordEncoder = new PasswordEncoderConfig();

		return args -> {
			// 유저 생성
			List<User> memberList = new ArrayList<>();

			User user1 = User.builder()
				.username("user1")
				.password(passwordEncoder.passwordEncoder().encode("123"))
				.email("user1@test.com")
				.build();

			User user2 = User.builder()
				.username("user2")
				.password(passwordEncoder.passwordEncoder().encode("123!"))
				.email("user2@test.com")
				.build();

			memberList.addAll(List.of(user1, user2));
			memberRepository.saveAll(memberList);

			// 유저의 지출 등록
			List<Expenditure> expendityreList = new ArrayList<>();

			for (int i = 10; i <= 20; i++) {

				Expenditure 식비 = Expenditure.builder()
						.userId(user1.getId())
						.amount( i * 1000 )
						.category(Category.음식)
						.memo("카페")
						.date(LocalDate.of(2023,11, i))
						.build();

				Expenditure 교통비 = Expenditure.builder()
						.userId(user1.getId())
						.amount( 1800 )
						.category(Category.교통)
						.memo("버스비")
						.date(LocalDate.of(2023,11, i))
						.build();

				expendityreList.add(식비);
				// 왕복 교총비
				expendityreList.add(교통비);
				expendityreList.add(교통비);
			}

			Expenditure 가스비 = Expenditure.builder()
					.userId(user1.getId())
					.amount( 50000 )
					.category(Category.세금)
					.memo("가스비")
					.date(LocalDate.of(2023,11, 10))
					.build();

			expendityreList.add(가스비);

			Expenditure 경조사 = Expenditure.builder()
					.userId(user1.getId())
					.amount( 50000 )
					.category(Category.경조사)
					.memo("서녜 결혼 축하해!")
					.date(LocalDate.of(2023,11, 18))
					.build();

			expendityreList.add(경조사);

			Expenditure 도서구입비 = Expenditure.builder()
					.userId(user1.getId())
					.amount( 50000 )
					.category(Category.교육)
					.memo("교보문고에서 CS책 샀음")
					.date(LocalDate.of(2023,11, 12))
					.build();

			expendityreList.add(도서구입비);

			expenditureRepository.saveAll(expendityreList);
		};
	}
}