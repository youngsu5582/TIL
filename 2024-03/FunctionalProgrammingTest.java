package woowacourse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * 함수형 프로그래밍이란 관심사를 분리하여 프로그래밍하는 방법으로 작업을 어떻게 수행할 것인지, 즉 로직에 집중하는 것이 아니라 무엇을 할 것인지에 집중합니다.
 * 구체적인 작업 방식은 라이브러리가 알아서 처리하고, 사용자는 라이브러리가 제공하는 인터페이스를 구현하는 것만으로도 원하는 작업을 수행할 수 있습니다.
 * <p>
 * 참고: <a href="https://en.wikipedia.org/wiki/Functional_programming">함수형 프로그래밍</a>
 * 참고: <a href="https://en.wikipedia.org/wiki/Separation_of_concerns">관심사 분리</a>
 */
public class FunctionalProgrammingTest {
    /**
     * 먼저 함수형 프로그래밍을 지원하는 자바의 문법을 학습합니다.
     */
    @Nested
    @DisplayName("문법 학습 테스트")
    class SyntaxTest {
        /**
         * 익명 클래스는 인터페이스를 구현하는 클래스를 생성할 때 사용합니다.
         * <p>
         * 참고: <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html">Anonymous Classes</a>
         */
        @Test
        @DisplayName("익명 클래스")
        void 익명_클래스() {
            /**
             * Runnable 인터페이스를 구현하는 익명 클래스를 생성합니다.
             * 익명 클래스는 이름이 없으므로, 생성자를 호출할 때 바로 인터페이스를 구현하는 클래스를 생성합니다.
             */
            final var runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println("익명 클래스");
                }
            };
            runnable.run();
        }

        /**
         * 익명 클래스는 언어에서 미리 만들어둔 인터페이스만 구현하는 것이 아닌 사용자가 만든 인터페이스도 구현할 수 있습니다.
         * 꼭 메서드가 하나일 필요는 없습니다. 여러 개의 메서드를 구현할 수 있습니다.
         */
        @Test
        @DisplayName("사용자가 만든 인터페이스를 구현하는 익명 클래스")
        void 사용자가_만든_인터페이스를_구현하는_익명_클래스() {
            interface CustomAnonymousClass {
                void call();

                void run();
            }
            var result = 5;
            Function<Integer, Boolean> function = integer -> {
                System.out.println(this);
                return true;
            };
            final var customAnonymousClass = new CustomAnonymousClass() {


                @Override
                public void call() {
                    System.out.println(this);
                    System.out.println("CALL: 사용자가 만든 인터페이스를 구현하는 익명 클래스");
                }

                @Override
                public void run() {
                    System.out.println("RUN: 사용자가 만든 인터페이스를 구현하는 익명 클래스");
                }
            };
            customAnonymousClass.call();
            customAnonymousClass.run();
        }

        /**
         * 함수형 인터페이스는 하나의 추상 메서드만을 가지는 인터페이스입니다.
         * 함수형 인터페이스는 람다로 구현할 수 있습니다.
         * 람다는 익명 클래스를 더 간결하게 표현할 수 있는 방법으로, 자바8부터 함수형 프로그래밍을 지원하기 위해 추가된 기능입니다.
         * 하지만 람다는 익명 클래스와 달리 여러 개의 메서드를 구현할 수 없습니다.
         * <p>
         * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html">FunctionalInterface</a>
         */
        @Test
        @DisplayName("함수형 인터페이스")
        void 함수형_인터페이스() {
            @java.lang.FunctionalInterface
            interface FunctionalInterface {
                void call();
            }

            final FunctionalInterface functionalInterface1 = new FunctionalInterface() {
                @Override
                public void call() {
                    System.out.println("1");
                }
            };
            final FunctionalInterface functionalInterface2 = () -> System.out.println("1");

            functionalInterface1.call();

            /* Note: 여러 개의 메서드를 구현한 경우 함수형 인터페이스가 아니므로 람다로 구현할 수 없습니다.
            interface MultipleLambda {
                void call();

                void run();
            }

            final MultipleFunctionalInterface functionalInterface = () -> System.out.println("함수형 인터페이스");
             */
        }

        /**
         * Collections.sort() 메서드는 Comparator 인터페이스를 구현하는 클래스를 인자로 받습니다.
         * Comparator 인터페이스는 두 개의 인자를 받아서 비교하는 compare() 메서드를 가지고 있습니다.
         * 위에서 학습한 내용을 바탕으로 `User` 클래스의 나이를 기준으로 정렬해보세요.
         * <p>
         * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html">Comparator</a>
         * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#sort-java.util.List-java.util.Comparator-">Collections.sort()</a>
         */
        @Test
        @DisplayName("User의 나이를 기준으로 정렬한다")
        void User의_나이를_기준으로_정렬한다() {
            record User(String name, int age) {
            }

            final var brown = new User("Brown", 78);
            final var neo = new User("Neo", 90);
            final var brie = new User("Brie", 12);

            // TODO: 아래 코드를 Comparator를 구현하는 익명 클래스로 변경하여 User의 나이를 기준으로 정렬하세요. 람다로도 구현해보세요.
//
            List<User> users = new ArrayList<>(List.of(brown, neo, brie));

            users.sort(new Comparator<User>() {
                @Override
                public int compare(final User o1, final User o2) {
                    if (o1.age > o2.age) {
                        return 1;
                    }
                    if (o1.age == o2.age) {
                        return 0;
                    }
                    return -1;
                }
            });

//            users.sort(Comparator.comparingInt(value -> value.age));

//            users.sort((o1, o2) -> {
//                return o1.age > o2.age ? 1 : o1.age == o2.age ? 0 : -1;
//            });

            Collections.sort(users, Comparator.comparing(user -> user.age));

            for (final var user : users) {
                System.out.println(user.name() + ": " + user.age());
            }

            assertAll(
                    () -> assertThat(users).last()
                                           .isSameAs(neo),
                    () -> assertThat(users).first()
                                           .isSameAs(brie)
            );
        }
    }

    /**
     * 함수형 프로그래밍을 지원하는 자바의 라이브러리를 학습합니다.
     * <p>
     * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">java.util.function</a>
     * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html">java.util.stream</a>
     */
    @Nested
    @DisplayName("라이브러리 학습 테스트")
    class LibraryTest {
        /**
         * Function은 인자를 받아서 값을 반환하는 함수형 인터페이스입니다.
         * 입력값을 받아서 출력값을 반환하는 함수를 표현할 때 사용합니다.
         * <p>
         * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html">Function</a>
         */
        private record User(int age) {

        }

        @Test
        @DisplayName("Function")
        void Function() {
            Stream.builder()
                  .add(new User(5))
                  .add(new User(3))
                  .build()
                  .forEach(System.out::println);
        }

        /**
         * Consumer는 인자를 받아서 소비하는 함수형 인터페이스입니다.
         * 입력값을 받아서 출력값을 반환하지 않고, 입력값을 소비하는 함수를 표현할 때 사용합니다.
         * <p>
         * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html">Consumer</a>
         */
        @Test
        @DisplayName("Consumer")
        void Consumer() {
            final Consumer<String> consumer = value -> System.out.println(value);
            consumer.accept("Consumer");
        }

        /**
         * Supplier는 인자를 받지 않고 값을 반환하는 함수형 인터페이스입니다.
         * 입력값 없이 출력값만 있는 함수를 표현할 때 사용합니다.
         * <p>
         * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html">Supplier</a>
         */
        @Test
        @DisplayName("Supplier")
        void Supplier() {
            final Supplier<String> supplier = () -> "Supplier";

            System.out.println(supplier.get());
        }

        /**
         * Predicate는 인자를 받아서 boolean 값을 반환하는 함수형 인터페이스입니다.
         * 입력값을 받아서 boolean 값을 반환하는 함수를 표현할 때 사용합니다.
         * <p>
         * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html">Predicate</a>
         */
        @Test
        @DisplayName("Predicate")
        void Predicate() {
            final Predicate<String> predicate = value -> value.equals("Predicate");
            System.out.println(predicate.test("Predicate"));
        }

        /**
         * Optional은 null을 대체할 수 있는 자바의 라이브러리입니다.
         * <p>
         * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html">Optional</a>
         */
        @Test
        @DisplayName("Optional")
        void Optional() {
            final var optional = Optional.of("Optional");
            System.out.println(optional.get());
        }

        /**
         * 기존 함수의 중복을 람다를 활용해 중복을 제거해봅니다.
         * 과정을 통해 람다 사용법을 학습합니다.
         */
        @Test
        @DisplayName("기존 함수의 중복을 람다를 활용해 중복을 제거한다")
        void 기존_함수의_중복을_람다를_활용해_중복을_제거한다() {
            class Calculator {
                // TODO: 람다를 활용하여 sum 메서드를 통해 중복을 제거하세요.
                static int sumAll(final List<Integer> numbers) {
                    return numbers.stream()
                                  .mapToInt(Integer::intValue)
                                  .sum();
                }

                // TODO: 람다를 활용하여 sum 메서드를 통해 중복을 제거하세요.
                static int sumAllEven(final List<Integer> numbers) {
                    return numbers.stream()
                                  .filter(number -> number % 2 == 0)
                                  .mapToInt(Integer::intValue)
                                  .sum();

                }

                // TODO: 람다를 활용하여 sum 메서드를 통해 중복을 제거하세요.
                static int sumAllOverThree(final List<Integer> numbers) {
                    return numbers.stream()
                                  .filter(number -> number > 3)
                                  .mapToInt(Integer::intValue)
                                  .sum();
                }

                private static int sum(
                        final List<Integer> numbers,
                        final Predicate<Integer> condition
                ) {
                    // TODO: 조건에 맞게 필터링하여 합계를 구하는 기능을 구현하세요.
                    return numbers.stream()
                                  .filter(condition)
                                  .mapToInt(Integer::intValue)
                                  .sum();
                }
            }

            final List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

            assertAll(
                    () -> assertThat(Calculator.sumAll(numbers)).isEqualTo(45),
                    () -> assertThat(Calculator.sumAllEven(numbers)).isEqualTo(20),
                    () -> assertThat(Calculator.sumAllOverThree(numbers)).isEqualTo(39)
            );
        }

        /*
        Function, Consumer, Supplier, Predicate, Optional은 대표적인 함수형 인터페이스입니다.
        이 외에도 다양한 함수형 인터페이스가 있습니다.
        <p>
        참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">java.util.function</a>
        */
    }

    /**
     * Stream은 자바의 컬렉션을 함수형 프로그래밍으로 다루기 위한 라이브러리입니다.
     * Stream API는 크게 생성, 가공, 소비의 세 가지 단계로 이루어져 있습니다.
     * <p>
     * 생성: 컬렉션을 스트림으로 변환합니다. 생성 단계에서 컬렉션을 스트림으로 변환하고, 가공 단계에서 스트림을 가공합니다.
     * 가공: 스트림을 가공합니다. 가공 단계에서는 스트림을 가공하는 메서드를 호출하고, 소비 단계에서는 가공된 스트림을 소비합니다.
     * 소비: 스트림을 소비합니다. 소비 단계에서는 가공된 스트림을 소비합니다.
     * <p>
     * 위에서 학습한 기능들을 생성, 가공, 소비 단계에서 사용할 수 있습니다.
     * <p>
     * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html">Stream</a>
     * 참고: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html">java.util.stream</a>
     */
    @Nested
    @DisplayName("Stream 학습 테스트")
    class StreamTest {
        /**
         * 스트림을 이용하면 선언형으로 컬렉션을 가공할 수 있습니다.
         * 기존 반복문과 조건문으로 컬렉션을 가공하는 방식은 명령형 프로그래밍이라고 하며, 스트림을 이용하여 선언형으로 컬렉션을 가공하는 방식은 선언형 프로그래밍이라고 합니다.
         * 선언형이란 어떻게 할 것인지를 명시하는 것이 아니라 무엇을 할 것인지를 명시하는 것입니다.
         * <p>
         * 유저들 중 성인인 유저들의 이름을 나이순으로 정렬하여 출력하는 기능을 보며 기존 명령형으로 작성되어 있는 코드를 선언형으로 변경하는 예시를 살펴봅니다.
         * <p>
         * 참고: <a href="https://en.wikipedia.org/wiki/Declarative_programming">선언형 프로그래밍</a>
         * 참고: <a href="https://en.wikipedia.org/wiki/Imperative_programming">명령형 프로그래밍</a>
         */
        @Test
        @DisplayName("성인인 유저들의 이름을 나이순으로 정렬하여 출력한다")
        void 성인인_유저들의_이름을_나이순으로_정렬하여_출력한다() {
            record User(String name, int age) {
            }

            final var users = List.of(
                    new User("Brown", 78),
                    new User("Neo", 90),
                    new User("Brie", 12)
            );


            // Note: 명령형으로 작성된 코드
            // Note: 성인 필터
            final var filteredUsers = new ArrayList<User>(users);
            for (final var user : users) {
                if (user.age() < 20) {
                    filteredUsers.remove(user);
                }
            }
            users.stream()
                 .filter(user -> user.age >= 20)
                 .toList();

            // Note: 나이 기준으로 정렬
            final var sortedUsers = new ArrayList<User>(filteredUsers);
            for (int i = 0, end = sortedUsers.size(); i < end; i++) {
                for (int j = i + 1; j < end; j++) {
                    if (sortedUsers.get(i)
                                   .age() > sortedUsers.get(j)
                                                       .age()) {
                        final var temp = sortedUsers.get(i);
                        sortedUsers.set(i, users.get(j));
                        sortedUsers.set(j, temp);
                    }
                }
            }
            users.stream()
                 .sorted(Comparator.comparingInt(User::age));


            // Note: 이름 추출
            users.stream()
                 .map(User::name)
                 .toList();

            // Note: 출력
            users.forEach(System.out::println);

            // -----------------------------------------------------------------

            // Note: 선언형으로 변경된 코드
            users.stream() // Note: 생성
                 .filter(user -> user.age() >= 20) // Note: 성인 필터 (가공)
                 .sorted(Comparator.comparing(User::age)) // Note: 나이 기준으로 정렬 (가공)
                 .map(User::name) // Note: 이름 추출 (가공)
                 .forEach(System.out::println); // Note: 출력 (소비)
        }

        /**
         * 선언형 프로그래밍은 어떻게 할 것인지를 명시하는 명령형 프로그래밍과는 다르게 어떻게 할 것인지를 명시하지 않습니다.
         * 어떻게 할 것인지에 대한 구체적인 작업은 라이브러리가 결정하고, 개발자는 무엇을 수행할 것인지에 집중할 수 있기 때문에 가독성이 뛰어나고 유지보수가 쉽습니다.
         * <p>
         * 기존 명령형 프로그래밍으로 구현되어 있는 코드를 선언형으로 변경하며 선언형 프로그래밍을 연습해봅니다.
         * 크루들 중 이름이 김으로 시작하고 나이가 25세 이상 30세 미만이며 닉네임이 2글자인 크루들 중 가장 나이가 많은 크루를 찾아서 출력하는 기능을 보며 기존 명령형으로 작성되어 있는 코드를 선언형으로 변경하는 예시를 살펴봅니다.
         */
        @Test
        @DisplayName("명령형으로 작성된 코드를 선언형으로 변경한다")
        void 명령형으로_작성된_코드를_선언형으로_변경한다() {
            record Crew(String name, String nickname, int age) {
            }

            final var crews = List.of(
                    new Crew("류성현", "brown", 78),
                    new Crew("김재연", "neo", 90),
                    new Crew("김", "one", 24),
                    new Crew("김재연날리기", "tw", 30),
                    new Crew("김재", "th", 29),
                    new Crew("겁재", "fo", 25),
                    new Crew("조부용", "brie", 12)
            );

            // TODO: 아래 코드를 선언형으로 변경하세요.

            final var filteredCrews = new ArrayList<Crew>();
            for (final var crew : crews) {
                if (crew.name()
                        .startsWith("김") &&
                        crew.age() >= 25 &&
                        crew.age() < 30 &&
                        crew.nickname()
                            .length() == 2) {
                    filteredCrews.add(crew);
                }
            }
            Crew maxAgeCrew = crews.stream()
                                   .filter(crew -> crew.name.startsWith("김"))
                                   .filter(crew -> crew.age >= 25 && crew.age < 30)
                                   .filter(crew -> crew.nickname.length() == 2)
                                   .max(Comparator.comparingInt(Crew::age))
                                   .get();

//
//            var maxAge = 0;
//            Crew maxAgeCrew = null;
//            for (final var crew : filteredCrews) {
//                if (crew.age() > maxAge) {
//                    maxAge = crew.age();
//                    maxAgeCrew = crew;
//                }
//            }

            System.out.println(maxAgeCrew.name() + ": " + maxAgeCrew.nickname() + ": " + maxAgeCrew.age());

            // -----------------------------------------------------------------

            assertThat(maxAgeCrew).isEqualTo(new Crew("김재", "th", 29));
        }

        /**
         * 전쟁과 평화 내용 중 문자 길이가 12보다 큰 경우의 수를 구하는 기능을 구현해봅니다.
         * 가능하면 세미콜론을 한 번만 사용하여 구현해보세요.
         */
        @Test
        @DisplayName("Stream API를 활용하여 전쟁과 평화 내용 중 문자 길이가 12보다 큰 경우의 수를 구한다")
        void Stream_API를_활용하여_전쟁과_평화_내용_중_문자_길이가_12보다_큰_경우의_수를_구한다() throws IOException {
            final var contents = Files.readString(Paths.get("src/test/resources/war-and-peace.txt"));

            // TODO: 아래 코드를 Stream API를 활용하여 구현하세요.
            final var words = contents.split("\\P{L}+");
            long count = Arrays.stream(words)
                               .filter(word -> word.length() > 12)
                               .count();
            // -----------------------------------------------------------------

            assertThat(count).isEqualTo(1_946);
        }

        /**
         * for문을 활용하여 콜론을 추가하는 기능을 Stream API를 활용하여 구현해봅니다.
         * 가능하면 세미콜론을 한 번만 사용하여 구현해보세요.
         */
        @Test
        @DisplayName("for문을 활용하여 콜론을 추가하는 기능을 Stream API를 활용하여 구현한다")
        void for문을_활용하여_콜론을_추가하는_기능을_Stream_API를_활용하여_구현한다() {
            final var numbers = List.of(1, 2, 3, 4, 5);

            // TODO: 아래 코드를 Stream API를 활용하여 구현하세요.
            var result = numbers.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(":"));


            // -----------------------------------------------------------------

            assertThat(result).isEqualTo("1:2:3:4:5");
        }

        /**
         * for문을 활용하여 값을 필터링하고 가공하여 값을 구하는 기능을 Stream API를 활용하여 구현해봅니다.
         * 주어진 값 중 2보다 크고, 5보다 작거나 같은 수를 찾아서 2를 곱합니다. 그 값이 7보다 큰 모든 수의 합을 구하는 기능을 구현해주세요.
         * 가능하면 세미콜론을 한 번만 사용하여 구현해보세요.
         */
        @Test
        @DisplayName("for문을 활용하여 값을 필터링하고 가공하여 값을 구하는 기능을 Stream API를 활용하여 구현한다")
        void for문을_활용하여_값을_필터링하고_가공하여_값을_구하는_기능을_Stream_API를_활용하여_구현한다() {
            final var numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            final var expected = 18;

            // TODO: 아래 코드를 Stream API를 활용하여 구현하세요.
            var result = numbers.stream()
                                .filter(number -> 2 < number && number <= 5)
                                .filter(number -> number * 2 >= 7)
                                .mapToInt(value -> value * 2)
                                .sum();


            // -----------------------------------------------------------------

            assertThat(result).isEqualTo(expected);
        }

        /**
         * Stream API를 활용하여 전쟁과 평화 내용 중 아래 조건에 해당하는 단어를 찾아서 추출하는 기능을 구현해봅니다.
         * 가능하면 세미콜론을 한 번만 사용하여 구현해보세요.
         * <p>
         * ## 조건
         * - 단어의 길이가 12자를 초과하는 단어를 추출한다.
         * - 12자가 넘는 단어 중 길이가 짧은 순서대로 100개의 단어를 추출한다.
         * -- 단어 중복을 허용하지 않는다. 즉, 서로 다른 단어 100개를 추출한다.
         * - 추출한 단어의 두번째 문자가 대문자라면 단어를 제외한다.
         * -- ex) abc 허용 | abC 허용 | aBc 제외 | aBC 제외 | AbC 허용
         * - 추출한 단어를 소문자로 변경하여 알파벳 순서대로 앞에 있는 10개의 단어를 추출한다.
         */
        @Test
        @DisplayName("전쟁과 평화 내용 중 조건에 해당하는 단어를 찾아서 추출한다")
        void 전쟁과_평화_내용_중_아래_조건에_해당하는_단어를_찾아서_추출한다() throws IOException {
            final var contents = Files.readString(Paths.get("src/test/resources/war-and-peace.txt"));

            // TODO: 위 조건에 맞는 10개의 단어를 추출하세요.
            final var words = contents.split("\\P{L}+");
            var results = Arrays.stream(words)
                                .filter(word -> word.length() > 12)
                                .distinct()
                                .sorted(Comparator.comparing(String::length))
                                .limit(100)
                                .filter(word -> 'A' > word.charAt(1) || word.charAt(1) > 'Z')
                                .map(String::toLowerCase)
                                .sorted(Comparator.naturalOrder())
                                .limit(10)
                                .toList();

            // -----------------------------------------------------------------
            assertThat(results).containsExactly(
                    "acknowledging",
                    "acquaintances",
                    "argumentative",
                    "artificiality",
                    "assassination",
                    "buonapartists",
                    "ceremoniously",
                    "characterized",
                    "circumstances",
                    "commiseration"
            );
        }

        /**
         * Stream API를 활용하여 전쟁과 평화 내용 중 가장 많이 등장하는 단어의 수를 찾는 기능을 구현해봅니다.
         * 가능하면 세미콜론을 한 번만 사용하여 구현해보세요.
         */

        @Test
        @DisplayName("전쟁과 평화 내용 중 가장 많이 등장하는 단어의 수를 찾는다")
        void 전쟁과_평화_내용_중_가장_많이_등장하는_단어의_수를_찾는다() throws IOException {
            final var contents = Files.readString(Paths.get("src/test/resources/war-and-peace.txt"));

            // TODO: 가장 많이 등장하는 단어의 수를 찾으세요.
            final var words = contents.split("\\P{L}+");

            var result = Arrays.stream(words)
                               .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                               .values()
                               .stream()
                               .max(Comparator.naturalOrder())
                               .get();


            // -----------------------------------------------------------------
            assertThat(result).isEqualTo(31_949L);
        }

        /**
         * ----- 아래는 추가로 학습할 분만 보세요! -----
         * 함수 내부에서 외부의 상태를 변경하는 것을 사이드 이펙트라고 하며 사이드 이펙트가 발생하면 병렬 처리를 할 수 없습니다.
         * 외부의 상태를 변경하는 것은 스레드 간에 공유되는 메모리를 변경하는 것을 의미하며 스레드 간에 공유되는 메모리를 변경하면 동시성 문제가 발생할 수 있습니다.
         * 즉, 동시성 문제는 스레드 간에 공유되는 메모리를 변경하는 것을 의미합니다.
         * <p>
         * 사이드 이펙트가 없는 함수는 외부의 상태를 변경하지 않는 함수를 의미하며 같은 인자를 받으면 항상 같은 값을 반환하는데 이러한 함수를 순수 함수라고 합니다.
         * 선언형 프로그래밍의 경우 순수 함수를 사용하여 병렬 처리를 할 수 있습니다.
         * <p>
         * 참고: <a href="https://en.wikipedia.org/wiki/Side_effect_(computer_science)">사이드 이펙트</a>
         * 참고: <a href="https://en.wikipedia.org/wiki/Parallel_computing">병렬 처리</a>
         * 참고: <a href="https://en.wikipedia.org/wiki/Concurrency_(computer_science)">동시성</a>
         * 참고: <a href="https://en.wikipedia.org/wiki/Thread_(computing)">스레드</a>
         * 참고: <a href="https://en.wikipedia.org/wiki/Shared_memory">공유 메모리</a>
         * 참고: <a href="https://en.wikipedia.org/wiki/Pure_function">순수 함수</a>
         */
        @Test
        @DisplayName("사이드 이펙트가 없는 순수 함수의 병렬 처리")
        void 사이드_이펙트가_없는_순수_함수의_병렬_처리() throws InterruptedException {
            class Timer {
                public static long wait(final int waitSeconds) {
                    final var start = System.currentTimeMillis();
                    try {
                        Thread.sleep(waitSeconds * 1_000L);
                    } catch (final InterruptedException ignored) {
                    }

                    final var elapsed = System.currentTimeMillis() - start;
                    System.out.println(elapsed + "ms가 지났습니다.");

                    return elapsed;
                }
            }
            final var numbers = Stream.generate(() -> (int) (Math.random() * 5))
                                      .limit(10)
                                      .toList();

            // Note: 명령형으로 작성된 코드
            final var imperativeStart = System.currentTimeMillis();
            final var latch = new CountDownLatch(numbers.size());
            final var imperativeTotalElapsedMillis = new AtomicLong();
            for (final var number : numbers) {
                new Thread(() -> {
                    long elapsedMillis = Timer.wait(number);
                    synchronized (imperativeTotalElapsedMillis) {
                        imperativeTotalElapsedMillis.set(imperativeTotalElapsedMillis.get() + elapsedMillis);
                        latch.countDown();
                    }
                }).start();
            }
            latch.await();
            final var imperativeEnd = System.currentTimeMillis();
            System.out.println("명령형으로 " + imperativeTotalElapsedMillis.get() + "ms 처리 시 " + (imperativeEnd - imperativeStart) + "ms 걸렸습니다.");

            // -----------------------------------------------------------------

            // Note: 선언형으로 변경된 코드
            final var declarativeStart = System.currentTimeMillis();
            final var declarativeTotalElapsedMillis = numbers.parallelStream()
                                                             .map(Timer::wait)
                                                             .reduce(0L, Long::sum);
            final var declarativeEnd = System.currentTimeMillis();
            System.out.println("선언형으로 " + declarativeTotalElapsedMillis + "ms 처리 시 " + (declarativeEnd - declarativeStart) + "ms 걸렸습니다.");
        }
    }
}
