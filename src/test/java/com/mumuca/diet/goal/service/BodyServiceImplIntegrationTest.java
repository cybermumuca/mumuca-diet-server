package com.mumuca.diet.goal.service;

import com.mumuca.diet.auth.model.User;
import com.mumuca.diet.auth.repository.UserRepository;
import com.mumuca.diet.dto.body.BodyDTO;
import com.mumuca.diet.dto.body.BodyRegistryDTO;
import com.mumuca.diet.dto.body.BodyRegistryUpdateDTO;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.model.Body;
import com.mumuca.diet.repository.BodyRepository;
import com.mumuca.diet.service.BodyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.mumuca.diet.testutil.EntityGeneratorUtil.createUser;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("BodyServiceImpl Integration Tests")
public class BodyServiceImplIntegrationTest {

    @Autowired
    private BodyService sut;

    @Autowired
    private BodyRepository bodyRepository;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("registerBody tests")
    class RegisterBodyTests {

        @Test
        @Transactional
        @DisplayName("should be able to register a new body entry")
        void shouldBeAbleToRegisterANewBodyEntry() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            var today = LocalDate.now();

            BodyRegistryDTO bodyRegistryDTO = new BodyRegistryDTO(BigDecimal.valueOf(70.0), BigDecimal.valueOf(1.78), today);

            // Act
            BodyDTO result = sut.registerBody(bodyRegistryDTO, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isNotNull();
            assertThat(result.height()).isEqualTo(bodyRegistryDTO.height());
            assertThat(result.weight()).isEqualTo(bodyRegistryDTO.weight());
            assertThat(result.date()).isEqualTo(today);

            var bodyInDatabase = bodyRepository.findById(result.id()).orElseThrow();

            assertThat(bodyInDatabase.getWeight()).isEqualByComparingTo(bodyRegistryDTO.weight());
            assertThat(bodyInDatabase.getHeight()).isEqualByComparingTo(bodyRegistryDTO.height());
            assertThat(bodyInDatabase.getDate()).isEqualTo(today);
        }

        @Test
        @Transactional
        @DisplayName("should throw exception when registering body for non-existent user")
        void shouldThrowExceptionWhenUserDoesNotExist() {
            // Arrange
            String userId = "non-existent-user";
            BodyRegistryDTO bodyRegistryDTO = new BodyRegistryDTO(BigDecimal.valueOf(1.78), BigDecimal.valueOf(70.0), LocalDate.now());

            // Act & Assert
            assertThatThrownBy(() -> sut.registerBody(bodyRegistryDTO, userId));
        }
    }

    @Nested
    @DisplayName("getBodyRegistry tests")
    class GetBodyRegistryTests {

        @Test
        @Transactional
        @DisplayName("should be able to get body registry")
        void shouldBeAbleToGetBodyRegistry() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            var today = LocalDate.now();

            Body body = new Body();
            body.setUser(user);
            body.setHeight(BigDecimal.valueOf(1.78));
            body.setWeight(BigDecimal.valueOf(70.0));
            body.setDate(today);
            bodyRepository.save(body);

            // Act
            BodyDTO result = sut.getBodyRegistry(body.getId(), user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(body.getId());
            assertThat(result.height()).isEqualByComparingTo(body.getHeight());
            assertThat(result.weight()).isEqualByComparingTo(body.getWeight());
            assertThat(result.date()).isEqualTo(today);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException when body registry does not exist")
        void shouldThrowResourceNotFoundExceptionWhenBodyNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            // Act & Assert
            assertThatThrownBy(() -> sut.getBodyRegistry(randomUUID().toString(), user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Body registry not found.");
        }
    }

    @Nested
    @DisplayName("updateBodyRegistry tests")
    class UpdateBodyRegistryTests {

        @Test
        @Transactional
        @DisplayName("should be able to update body registry")
        void shouldBeAbleToUpdateBodyRegistry() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            var today = LocalDate.now();
            var updatedDate = today.plusDays(1);

            Body body = new Body();
            body.setUser(user);
            body.setHeight(BigDecimal.valueOf(1.78));
            body.setWeight(BigDecimal.valueOf(70.0));
            body.setDate(today);
            bodyRepository.save(body);

            BodyRegistryUpdateDTO bodyRegistryUpdateDTO = new BodyRegistryUpdateDTO(
                    BigDecimal.valueOf(72.0), BigDecimal.valueOf(1.80), updatedDate
            );

            // Act
            sut.updateBodyRegistry(body.getId(), bodyRegistryUpdateDTO, user.getId());

            // Assert
            var updatedBodyInDatabase = bodyRepository.findById(body.getId()).orElseThrow();

            assertThat(updatedBodyInDatabase.getId()).isEqualTo(body.getId());
            assertThat(updatedBodyInDatabase.getWeight()).isEqualByComparingTo(bodyRegistryUpdateDTO.weight());
            assertThat(updatedBodyInDatabase.getHeight()).isEqualByComparingTo(bodyRegistryUpdateDTO.height());
            assertThat(updatedBodyInDatabase.getDate()).isEqualTo(updatedDate);
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException when body registry does not exist")
        void shouldThrowResourceNotFoundExceptionWhenBodyNotFound() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            BodyRegistryUpdateDTO bodyRegistryUpdateDTO = new BodyRegistryUpdateDTO(
                    BigDecimal.valueOf(72.0), BigDecimal.valueOf(1.80), LocalDate.now()
            );

            // Act & Assert
            assertThatThrownBy(() -> sut.updateBodyRegistry(randomUUID().toString(), bodyRegistryUpdateDTO, user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Body registry not found.");
        }
    }

    @Nested
    @DisplayName("deleteBodyRegistry tests")
    class DeleteBodyRegistryTests {
        @Test
        @Transactional
        @DisplayName("should be able to delete body registry")
        void shouldBeAbleToDeleteBodyRegistry() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            var today = LocalDate.now();

            Body body = new Body();
            body.setUser(user);
            body.setHeight(BigDecimal.valueOf(1.78));
            body.setWeight(BigDecimal.valueOf(70.0));
            body.setDate(today);
            bodyRepository.save(body);

            // Act
            sut.deleteBodyRegistry(body.getId(), user.getId());

            // Assert
            assertThat(bodyRepository.findById(body.getId())).isEmpty();
        }

        @Test
        @Transactional
        @DisplayName("should throw ResourceNotFoundException if body does not exist")
        void shouldThrowResourceNotFoundExceptionIfBodyDoesNotExist() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            assertThatThrownBy(() -> sut.deleteBodyRegistry(randomUUID().toString(), user.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Body registry not found.");
        }
    }

    @Nested
    @DisplayName("getBodiesRegistry tests")
    class GetBodiesRegistryTests {

        @Test
        @Transactional
        @DisplayName("should be able to get a page of body registries")
        void shouldBeAbleToGetBodiesRegistry() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            var today = LocalDate.now();
            var tomorrow = today.plusDays(1);

            Body body1 = new Body();
            body1.setUser(user);
            body1.setHeight(BigDecimal.valueOf(1.78));
            body1.setWeight(BigDecimal.valueOf(70.0));
            body1.setDate(today);
            bodyRepository.save(body1);

            Body body2 = new Body();
            body2.setUser(user);
            body2.setHeight(BigDecimal.valueOf(1.80));
            body2.setWeight(BigDecimal.valueOf(75.0));
            body2.setDate(tomorrow);
            bodyRepository.save(body2);

            Pageable pageable = PageRequest.of(0, 10);

            // Act
            Page<BodyDTO> result = sut.getBodiesRegistry(pageable, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getContent())
                    .hasSize(2)
                    .extracting(BodyDTO::id, BodyDTO::weight, BodyDTO::height, BodyDTO::date)
                    .containsExactlyInAnyOrder(
                            tuple(body1.getId(), body1.getWeight(), body1.getHeight(), body1.getDate()),
                            tuple(body2.getId(), body2.getWeight(), body2.getHeight(), body2.getDate())
                    );
        }

        @Test
        @Transactional
        @DisplayName("should return an empty page when no body registries exist")
        void shouldReturnEmptyPageWhenNoBodiesExist() {
            // Arrange
            User user = createUser();
            userRepository.save(user);

            Pageable pageable = PageRequest.of(0, 10);

            // Act
            Page<BodyDTO> result = sut.getBodiesRegistry(pageable, user.getId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getContent()).isEmpty();
        }
    }
}
