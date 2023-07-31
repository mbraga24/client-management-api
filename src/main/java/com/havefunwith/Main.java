package com.havefunwith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
@RestController
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/greeting")
    public String greet() {
        return "Hello there!";
    }

    @GetMapping("/greetResponse")
    public GreetResponse greetResponse() {
        return new GreetResponse(
                "Hey there!",
                List.of("Java", "JavaScript", "Python"),
                new Person("Michael", 27, 30_000));
    }

    record Person(String name, int age, double savings) {}
//    record GreetResponse(
//            String greet,
//            List<String> favProgrammingLanguages,
//            Person person) {}

    // This class will function the same way as the record GreetResponse created above
    class GreetResponse {
        private final String greet;
        private final List<String> favProgrammingLanguages;
        private final Person person;

        public GreetResponse(String greet, List<String> favProgrammingLanguages, Person person) {
            this.greet = greet;
            this.favProgrammingLanguages = favProgrammingLanguages;
            this.person = person;
        }

        public String getGreet() {
            return greet;
        }

        public List<String> getFavProgrammingLanguages() {
            return favProgrammingLanguages;
        }

        public Person getPerson() {
            return person;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GreetResponse that = (GreetResponse) o;
            return Objects.equals(greet, that.greet) && Objects.equals(favProgrammingLanguages, that.favProgrammingLanguages) && Objects.equals(person, that.person);
        }

        @Override
        public int hashCode() {
            return Objects.hash(greet, favProgrammingLanguages, person);
        }

        @Override
        public String toString() {
            return "GreetResponse{" +
                    "greet='" + greet + '\'' +
                    ", favProgrammingLanguages=" + favProgrammingLanguages +
                    ", person=" + person +
                    '}';
        }
    }
}
