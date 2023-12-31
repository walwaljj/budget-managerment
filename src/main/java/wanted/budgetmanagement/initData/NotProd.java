package wanted.budgetmanagement.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import wanted.budgetmanagement.config.security.PasswordEncoderConfig;
import wanted.budgetmanagement.domain.Category;
import wanted.budgetmanagement.domain.budget.entity.Budget;
import wanted.budgetmanagement.domain.budget.entity.BudgetDetail;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;
import wanted.budgetmanagement.domain.user.entity.Alert;
import wanted.budgetmanagement.domain.user.entity.User;
import wanted.budgetmanagement.repository.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               ExpenditureRepository expenditureRepository,
                               BudgetRepository budgetRepository,
                               AlertRepository alertRepository,
                               BudgetDetailRepository budgetDetailRepository) {

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

            User user3 = User.builder()
                    .username("user3")
                    .password(passwordEncoder.passwordEncoder().encode("123"))
                    .email("user3@test.com")
                    .build();

            memberList.addAll(List.of(user1, user2, user3));
            userRepository.saveAll(memberList);


            Alert user1Alert = Alert.builder()
                    .alarmEnabled(true)
                    .webHookUrl("https://discord.com/api/webhooks/1182224383582937118/IFOI3kLvDpb5_V7hcR7_Nk-rQ20JC_nqKr3xIAp1-lJkxBxE1Zmf3ZUDAuOTd98y7KZ0")
                    .userId(user1.getId())
                    .build();

            Alert user2Alert = Alert.builder()
                    .alarmEnabled(true)
                    .webHookUrl("https://discord.com/api/webhooks/1098767166314119289/OSsHdyROnkkfwInv8VoCDJ2KBrzo8jwJOEo3Hik3hY8p73PSXmzwrtNdsqnp-ISdRhZ7")
                    .userId(user2.getId())
                    .build();

            alertRepository.save(user1Alert);
            alertRepository.save(user2Alert);

            // 유저의 지출 등록
            List<Expenditure> expendityreList = new ArrayList<>();


            for (int i = 10; i <= 12; i++) { // 월
                for (int j = 1; j <= 30; j++) { // 일
                    for (int k = 1; k <= 2; k++) { // user
                        String username = "user" + k;

                        User user = userRepository.findByUsername(username).get();

                        if (i == 11 && user.getUsername().equals("user2")) { //user2의 10월 대비 11월 지출을 늘리기 위함.
                            Expenditure 저녁 = Expenditure.builder()
                                    .userId(user.getId())
                                    .amount(10000)
                                    .category(Category.음식)
                                    .memo("저녁")
                                    .date(LocalDate.of(2023, 11, j))
                                    .build();

                            expendityreList.add(저녁);
                        }

                        Expenditure 식비 = Expenditure.builder()
                                .userId(user.getId())
                                .amount(j * 1000)
                                .category(Category.음식)
                                .memo("카페")
                                .date(LocalDate.of(2023, i, j))
                                .build();

                        Expenditure 출근 = Expenditure.builder()
                                .userId(user.getId())
                                .amount(1800)
                                .category(Category.교통)
                                .memo("버스비")
                                .date(LocalDate.of(2023, i, j))
                                .build();

                        Expenditure 퇴근 = Expenditure.builder()
                                .userId(user.getId())
                                .amount(1800)
                                .category(Category.교통)
                                .memo("버스비")
                                .date(LocalDate.of(2023, i, j))
                                .build();

                        expendityreList.add(식비);
                        // 왕복 교통비
                        expendityreList.add(출근);
                        expendityreList.add(퇴근);
                    }
                }
            }


            Expenditure 식비 = Expenditure.builder()
                    .userId(user1.getId())
                    .amount(14000)
                    .category(Category.음식)
                    .memo("엽떡")
                    .date(LocalDate.of(2023, 11, 18))
                    .build();

            expendityreList.add(식비);

            Expenditure 가스비 = Expenditure.builder()
                    .userId(user1.getId())
                    .amount(50000)
                    .category(Category.세금)
                    .memo("가스비")
                    .date(LocalDate.of(2023, 11, 10))
                    .build();

            expendityreList.add(가스비);

            Expenditure 경조사 = Expenditure.builder()
                    .userId(user1.getId())
                    .amount(50000)
                    .category(Category.경조사)
                    .memo("서녜 결혼 축하해!")
                    .date(LocalDate.of(2023, 11, 18))
                    .build();

            expendityreList.add(경조사);

            Expenditure 도서구입비 = Expenditure.builder()
                    .userId(user1.getId())
                    .amount(50000)
                    .category(Category.교육)
                    .memo("교보문고에서 CS책 샀음")
                    .date(LocalDate.of(2023, 11, 12))
                    .build();

            expendityreList.add(도서구입비);

            expenditureRepository.saveAll(expendityreList);

            List<Budget> budgetList = new ArrayList<>();
            
            int user1Budget = 1000000;
            int user2Budget = 600000;
            int user3Budget = 400000;

            List<BudgetDetail> user1BudgetList = budgetDetailRepository.saveAll(getBudgetDetails(user1Budget));
            Budget budget1 = Budget.builder()
                    .userId(user1.getId())
                    .budgetDetails(user1BudgetList)
                    .month(LocalDate.now().getMonth())
                    .budget(user1Budget)
                    .build();

            // 예산 생성
            List<BudgetDetail> user2BudgetList = budgetDetailRepository.saveAll(getBudgetDetails(user2Budget));
            Budget budget2 = Budget.builder()
                    .userId(user2.getId())
                    .budgetDetails(user2BudgetList)
                    .month(LocalDate.now().getMonth())
                    .budget(user2Budget)
                    .build();

            // 예산 생성
            List<BudgetDetail> user3BudgetList = budgetDetailRepository.saveAll(getBudgetDetails(user3Budget));
            Budget budget3 = Budget.builder()
                    .userId(user3.getId())
                    .budgetDetails(user3BudgetList)
                    .month(LocalDate.now().getMonth())
                    .budget(user3Budget)
                    .build();

            budgetList.addAll(List.of(budget1, budget2, budget3));
            budgetRepository.saveAll(budgetList);
        };
    }

    private static List<BudgetDetail> getBudgetDetails(int userBudget) {
        // 예산 생성
        BudgetDetail 음식예산 = BudgetDetail.builder()
                .percent(90)
                .amount(userBudget / 100 * 90)
                .category(Category.음식)
                .month(Month.DECEMBER)
                .build();

        BudgetDetail 세금예산 = BudgetDetail.builder()
                .percent(10)
                .amount(userBudget / 100 * 10)
                .category(Category.세금)
                .month(Month.DECEMBER)
                .build();
        Result result = new Result(음식예산, 세금예산);

        List<BudgetDetail> budgetDetailList = new ArrayList<>();
        budgetDetailList.addAll(List.of(result.음식예산(), result.세금예산()));

        return budgetDetailList;
    }

    private record Result(BudgetDetail 음식예산, BudgetDetail 세금예산) {
    }
}