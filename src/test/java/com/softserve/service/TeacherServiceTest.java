package com.softserve.service;
import com.softserve.dto.TeacherDTO;
import com.softserve.dto.TeacherForUpdateDTO;
import com.softserve.dto.UserDataDTO;
import com.softserve.entity.Teacher;
import com.softserve.entity.User;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldNullException;
import com.softserve.mapper.TeacherMapper;
import com.softserve.repository.DepartmentRepository;
import com.softserve.repository.TeacherRepository;
import com.softserve.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.softserve.entity.enums.Role.ROLE_TEACHER;
import static com.softserve.entity.enums.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private UserService userService;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    private Teacher teacherWithoutId;
    private Teacher teacherWithId1LAndWithUserId1;
    private Teacher teacherWithId1LAndWithoutUser;

    private TeacherDTO teacherDtoWithoutId;
    private TeacherDTO teacherDtoWithId1L;
    private TeacherForUpdateDTO teacherForUpdateDTOWithId1L;
    private TeacherForUpdateDTO teacherForUpdateDTOResult;

    @BeforeEach
    void setUp() {
        String name = "Name1";
        String surname = "Surname1";
        String patronymic = "Patronymic1";
        String position = "Position1";
        String email = "teacher@gmail.com";

        teacherWithoutId = new Teacher();
        teacherWithoutId.setName(name);
        teacherWithoutId.setDisable(false);
        teacherWithoutId.setSurname(surname);
        teacherWithoutId.setPatronymic(patronymic);
        teacherWithoutId.setPosition(position);
        teacherWithoutId.setUserId(null);

        teacherWithId1LAndWithUserId1 = new Teacher();
        teacherWithId1LAndWithUserId1.setId(1L);
        teacherWithId1LAndWithUserId1.setDisable(false);
        teacherWithId1LAndWithUserId1.setName(name);
        teacherWithId1LAndWithUserId1.setSurname(surname);
        teacherWithId1LAndWithUserId1.setPatronymic(patronymic);
        teacherWithId1LAndWithUserId1.setPosition(position);
        teacherWithId1LAndWithUserId1.setUserId(1L);

        teacherWithId1LAndWithoutUser = new Teacher();
        teacherWithId1LAndWithoutUser.setId(1L);
        teacherWithId1LAndWithoutUser.setDisable(false);
        teacherWithId1LAndWithoutUser.setName(name);
        teacherWithId1LAndWithoutUser.setSurname(surname);
        teacherWithId1LAndWithoutUser.setPatronymic(patronymic);
        teacherWithId1LAndWithoutUser.setPosition(position);
        teacherWithId1LAndWithoutUser.setUserId(null);

        teacherDtoWithoutId = new TeacherDTO();
        teacherDtoWithoutId.setName(name);
        teacherDtoWithoutId.setDisable(false);
        teacherDtoWithoutId.setSurname(surname);
        teacherDtoWithoutId.setPatronymic(patronymic);
        teacherDtoWithoutId.setPosition(position);
        teacherDtoWithoutId.setEmail(email);

        teacherDtoWithId1L = new TeacherDTO();
        teacherDtoWithId1L.setId(1L);
        teacherDtoWithId1L.setName(name);
        teacherDtoWithId1L.setDisable(false);
        teacherDtoWithId1L.setSurname(surname);
        teacherDtoWithId1L.setPatronymic(patronymic);
        teacherDtoWithId1L.setPosition(position);
        teacherDtoWithId1L.setEmail(email);

        teacherForUpdateDTOWithId1L = new TeacherForUpdateDTO();
        teacherForUpdateDTOWithId1L.setId(1L);
        teacherForUpdateDTOWithId1L.setDisable(false);
        teacherForUpdateDTOWithId1L.setName(name);
        teacherForUpdateDTOWithId1L.setSurname(surname);
        teacherForUpdateDTOWithId1L.setPatronymic(patronymic);
        teacherForUpdateDTOWithId1L.setPosition(position);
        teacherForUpdateDTOWithId1L.setEmail(email);

        teacherForUpdateDTOResult = new TeacherForUpdateDTO();
        teacherForUpdateDTOResult.setId(1L);
        teacherForUpdateDTOResult.setDisable(false);
        teacherForUpdateDTOResult.setName(name);
        teacherForUpdateDTOResult.setSurname(surname);
        teacherForUpdateDTOResult.setPatronymic(patronymic);
        teacherForUpdateDTOResult.setPosition(position);
        teacherForUpdateDTOResult.setEmail(email);
    }

    @Nested
    class GetMethodsTests {

        @Test
        void getAll() {
            List<Teacher> teachers = Collections.singletonList(teacherWithId1LAndWithUserId1);
            List<TeacherDTO> expectedDTOs = Collections.singletonList(teacherDtoWithId1L);

            when(teacherRepository.getAll()).thenReturn(teachers);
            when(teacherMapper.teachersToTeacherDTOs(teachers)).thenReturn(expectedDTOs);

            List<TeacherDTO> actualTeachers = teacherService.getAll();

            assertThat(actualTeachers).hasSameSizeAs(expectedDTOs).hasSameElementsAs(expectedDTOs);

            verify(teacherRepository, times(1)).getAll();
            verify(teacherMapper, times(1)).teachersToTeacherDTOs(teachers);
        }

        @Test
        void getDisabled() {
            List<Teacher> teachers = Collections.singletonList(teacherWithId1LAndWithUserId1);
            List<TeacherDTO> expectedDTOs = Collections.singletonList(teacherDtoWithId1L);

            when(teacherRepository.getDisabled()).thenReturn(teachers);
            when(teacherMapper.teachersToTeacherDTOs(teachers)).thenReturn(expectedDTOs);

            List<TeacherDTO> actualTeachers = teacherService.getDisabled();

            assertThat(actualTeachers).hasSameSizeAs(expectedDTOs).hasSameElementsAs(expectedDTOs);

            verify(teacherRepository, times(1)).getDisabled();
            verify(teacherMapper, times(1)).teachersToTeacherDTOs(teachers);
        }

        @Test
        void getTeachersWithoutUsers() {
            List<Teacher> teachers = Collections.singletonList(teacherWithId1LAndWithoutUser);
            List<TeacherDTO> expectedDTOs = Collections.singletonList(teacherDtoWithId1L);

            when(teacherRepository.getAllTeacherWithoutUser()).thenReturn(teachers);
            when(teacherMapper.teachersToTeacherDTOs(teachers)).thenReturn(expectedDTOs);

            List<TeacherDTO> actualTeachers = teacherService.getAllTeacherWithoutUser();

            assertThat(actualTeachers).hasSameSizeAs(expectedDTOs).hasSameElementsAs(expectedDTOs);

            verify(teacherRepository, times(1)).getAllTeacherWithoutUser();
            verify(teacherMapper, times(1)).teachersToTeacherDTOs(teachers);
        }

        @Test
        void getById() {
            Teacher teacher = teacherWithId1LAndWithUserId1;

            when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
            when(teacherMapper.teacherToTeacherDTO(teacher)).thenReturn(teacherDtoWithId1L);

            TeacherDTO actualTeacher = teacherService.getById(1L);

            assertThat(actualTeacher)
                    .usingRecursiveComparison()
                    .isEqualTo(teacherDtoWithId1L);

            verify(teacherRepository, times(1)).findById(1L);
            verify(teacherMapper, times(1)).teacherToTeacherDTO(teacher);
        }

        @Test
        void throwEntityNotFoundExceptionIfTeacherNotFoundedById() {
            when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> teacherService.getById(1L));

            verify(teacherRepository, times(1)).findById(1L);
        }
    }

    @Nested
    class SaveTests {

        @ParameterizedTest
        @NullAndEmptySource
        void saveDTOIfEmailNotExist(String teacherEmail) {
            TeacherDTO inputDTO = new TeacherDTO();
            inputDTO.setName(teacherDtoWithoutId.getName());
            inputDTO.setSurname(teacherDtoWithoutId.getSurname());
            inputDTO.setPatronymic(teacherDtoWithoutId.getPatronymic());
            inputDTO.setPosition(teacherDtoWithoutId.getPosition());
            inputDTO.setEmail(teacherEmail);

            Teacher teacherEntity = teacherWithId1LAndWithoutUser;

            when(teacherMapper.teacherDTOToTeacher(inputDTO)).thenReturn(teacherEntity);
            when(teacherRepository.save(teacherEntity)).thenReturn(teacherEntity);
            when(teacherMapper.teacherToTeacherDTO(teacherEntity)).thenReturn(teacherDtoWithId1L);

            TeacherDTO actualTeacher = teacherService.save(inputDTO);

            assertThat(actualTeacher)
                    .usingRecursiveComparison()
                    .isEqualTo(teacherDtoWithId1L);

            verify(teacherRepository, times(1)).save(teacherEntity);
            verify(teacherMapper, times(1)).teacherDTOToTeacher(inputDTO);
            verify(teacherMapper, times(1)).teacherToTeacherDTO(teacherEntity);
        }

        @Test
        void saveDTOIfEmailExists() {
            TeacherDTO inputDTO = teacherDtoWithoutId;
            Teacher teacherAfterMapper = teacherWithoutId;
            Teacher savedTeacher = teacherWithId1LAndWithUserId1;

            User userForTeacher = new User();
            userForTeacher.setId(1L);

            when(teacherMapper.teacherDTOToTeacher(inputDTO)).thenReturn(teacherAfterMapper);

            when(userService.automaticRegistration(inputDTO.getEmail(), ROLE_TEACHER))
                    .thenReturn(userForTeacher);

            when(teacherRepository.save(argThat(
                    t -> t.getUserId() != null && t.getUserId().equals(1L))))
                    .thenReturn(savedTeacher);

            when(teacherMapper.teacherToTeacherDTO(savedTeacher))
                    .thenReturn(teacherDtoWithId1L);

            TeacherDTO actualTeacher = teacherService.save(inputDTO);

            assertThat(actualTeacher)
                    .usingRecursiveComparison()
                    .isEqualTo(teacherDtoWithId1L);

            verify(teacherRepository, times(1)).save(any(Teacher.class));
            verify(teacherMapper, times(1)).teacherDTOToTeacher(inputDTO);
            verify(userService, times(1))
                    .automaticRegistration(inputDTO.getEmail(), ROLE_TEACHER);

            verify(teacherMapper, times(1))
                    .teacherToTeacherDTO(savedTeacher);
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void deleteById() {
            Teacher teacher = teacherWithId1LAndWithoutUser;

            when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
            when(teacherRepository.delete(teacher)).thenReturn(teacher);

            teacherService.deleteById(1L);

            verify(teacherRepository, times(1)).findById(1L);
            verify(teacherRepository, times(1)).delete(teacher);
        }

        @Test
        void deleteByIdWhenTeacherHasUserIdShouldSetRoleUser() {
            Teacher teacher = teacherWithId1LAndWithUserId1;

            User user = new User();
            user.setId(1L);
            user.setRole(ROLE_TEACHER);

            when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
            when(userService.getById(1L)).thenReturn(user);
            when(teacherRepository.delete(teacher)).thenReturn(teacher);

            teacherService.deleteById(1L);

            assertEquals(ROLE_USER, user.getRole());

            verify(teacherRepository, times(1)).findById(1L);
            verify(userService, times(1)).getById(1L);
            verify(userService, times(1)).update(user);
            verify(teacherRepository, times(1)).delete(teacher);
        }
    }

    @Nested
    class UserDataTests {

        @Test
        void getUserDataByUserIdNullShouldReturnNull() {
            UserDataDTO result = teacherService.getUserDataByUserId(null);

            assertNull(result);

            verify(teacherRepository, never()).findByUserId(anyLong());
        }

        @Test
        void getUserDataByUserIdFoundTeacher() {
            Teacher teacher = teacherWithId1LAndWithUserId1;

            UserDataDTO userDataDTO = new UserDataDTO();
            userDataDTO.setTeacherId(1L);

            when(teacherRepository.findByUserId(1L))
                    .thenReturn(Optional.of(teacher));

            when(teacherMapper.teacherToUserDataDTO(teacher))
                    .thenReturn(userDataDTO);

            UserDataDTO result = teacherService.getUserDataByUserId(1L);

            assertNotNull(result);
            assertEquals(userDataDTO.getTeacherId(), result.getTeacherId());

            verify(teacherRepository, times(1)).findByUserId(1L);

            verify(teacherMapper, times(1))
                    .teacherToUserDataDTO(teacher);
        }

        @Test
        void getUserDataByUserIdNotFoundShouldReturnNull() {
            when(teacherRepository.findByUserId(1L))
                    .thenReturn(Optional.empty());

            UserDataDTO result = teacherService.getUserDataByUserId(1L);

            assertNull(result);

            verify(teacherRepository, times(1)).findByUserId(1L);

            verify(teacherMapper, never())
                    .teacherToUserDataDTO(any());
        }
    }

    @Nested
    class RemoveUserTests {

        @Test
        void removeUserFromTeacher() {
            Teacher teacher = teacherWithId1LAndWithUserId1;

            when(teacherRepository.findByUserId(1L))
                    .thenReturn(Optional.of(teacher));

            when(teacherRepository.update(
                    argThat(t -> t.getUserId() == null)))
                    .thenReturn(teacher);

            teacherService.removeUserFromTeacher(1L);

            verify(teacherRepository, times(1)).findByUserId(1L);

            verify(teacherRepository, times(1))
                    .update(argThat(t -> t.getUserId() == null));
        }

        @Test
        void removeUserFromTeacherNullShouldThrowFieldNullException() {
            assertThrows(FieldNullException.class,
                    () -> teacherService.removeUserFromTeacher(null));

            verify(teacherRepository, never()).update(any());
        }

        @Test
        void throwEntityNotFoundExceptionWhenRemoveUserFromNonExistentTeacher() {
            when(teacherRepository.findByUserId(1L))
                    .thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> teacherService.removeUserFromTeacher(1L));

            verify(teacherRepository, times(1)).findByUserId(1L);
        }
    }
}