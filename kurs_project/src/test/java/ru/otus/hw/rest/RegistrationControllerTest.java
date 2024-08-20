package ru.otus.hw.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.ChildDto;
import ru.otus.hw.dto.RegistrationPersonDto;
import ru.otus.hw.models.Child;
import ru.otus.hw.models.Person;
import ru.otus.hw.repositories.ChildRepository;
import ru.otus.hw.repositories.PersonRepository;
import ru.otus.hw.repositories.TeacherRepository;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(controllers = RegistrationController.class)
@TestPropertySource(properties = {"mongock.enabled=false"})
class RegistrationControllerTest {

    public static final String CHILD_ID_1 = "1";
    public static final String PERSON = "person fio";
    public static final String PERSON_SHORT = "person";
    public static final String PERSON_TEL = "12345678901";
    public static final String CHILD = "child  fio";
    public static final String CHILD_TEL = "45678901234";
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private ChildRepository childRepository;
    @MockBean
    private PersonRepository personRepository;
    @MockBean
    private TeacherRepository teacherRepository;


    @DisplayName("Регистрация ребенка")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void regPerson() {
        RegistrationPersonDto registrationPersonDto = new RegistrationPersonDto(PERSON, PERSON_TEL, CHILD, CHILD_TEL);
        Child child = new Child(null, CHILD, CHILD_TEL, new HashSet<>());
        Person person = new Person(null, PERSON, PERSON_TEL, new HashSet<>());


        given(childRepository.findByTelephone(any())).willReturn(Mono.empty());
        given(personRepository.findByTelephone(any())).willReturn(Mono.empty());
        given(childRepository.insert((Child) any())).willReturn(Mono.just(child));
        given(personRepository.insert((Person) any())).willReturn(Mono.just(person));
        given(personRepository.save(any())).willReturn(Mono.just(person));
        given(childRepository.save(any())).willReturn(Mono.just(child));

        var result = webTestClient.mutateWith(csrf())
                .post().uri("/api/reg/Person")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationPersonDto)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ChildDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<ChildDto> stepResult = step.expectNext(ChildDto.toDto(child));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("Регистрация ребенка")
    @Test
    @WithMockUser(username = "USER", authorities = {"ROLE_USER"})
    void badRequestWhenReg() {
        RegistrationPersonDto registrationPersonDto = new RegistrationPersonDto(PERSON_SHORT, PERSON_TEL, CHILD, CHILD_TEL);
        Child child = new Child(null, CHILD, CHILD_TEL, new HashSet<>());

        var result = webTestClient.mutateWith(csrf())
                .post().uri("/api/reg/Person")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationPersonDto)
                .exchange()
                .expectStatus().isBadRequest()
                .returnResult(ChildDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<ChildDto> stepResult = step.expectNext(ChildDto.toDto(child));
        assertThat(stepResult).isNotNull();
    }
}