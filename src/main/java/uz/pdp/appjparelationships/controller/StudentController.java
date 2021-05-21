package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SubjectRepository subjectRepository;


    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

//###################################### VAZIFA #########################################
    //3. FACULTY DEKANAT
    @GetMapping("/forDekanat/{facultyId}")
    public Page<Student> getStudentListforDekanat(@PathVariable Integer facultyId, @RequestParam Integer pagenumber) {
        Pageable pageable = PageRequest.of(pagenumber, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPage;
    }

    //4. GROUP OWNER
    @GetMapping("/foGroup/{groupId}")
    public Page<Student> getStudentListforGroup(@PathVariable Integer groupId, @RequestParam Integer pagenumber) {
        Pageable pageable = PageRequest.of(pagenumber, 10);
        Page<Student> studentPage = studentRepository.findAllByGroupId(groupId, pageable);
        return studentPage;
    }

    @PutMapping("/put/{id}")
    public String update(@PathVariable Integer id, StudentDto studentDto) {
        Optional<Student> optional = studentRepository.findById(id);
        if (optional.isPresent()) {
            Optional<Group> optional1 = groupRepository.findById(studentDto.getGroup_id());
            if (optional1.isPresent()) {
                Optional<Subject> optional2 = subjectRepository.findById(studentDto.getSubject_id());
                if (optional2.isPresent()) {
                    Student student = optional.get();
                    Address address = student.getAddress();
                    address.setCity(studentDto.getCity());
                    address.setDistrict(studentDto.getDistrict());
                    address.setStreet(studentDto.getStreet());
                    addressRepository.save(address);
                    Group group = optional1.get();
                    List<Subject> subjects = student.getSubjects();
                    subjects.remove(0);
                    subjects.add(optional2.get());
                    student.setAddress(address);
                    student.setGroup(group);
                    student.setFirstName(studentDto.getFirstname());
                    student.setLastName(student.getLastName());
                    student.setSubjects(subjects);
                    studentRepository.save(student);
                    return "Yangilandi";
                } else {
                    return "Bunday Subject topilmadi !!!";
                }
            } else {
                return "Bunday Group topilmadi !!!";
            }
        } else {
            return "Bunday Student topilmadi !!!";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Student> optional = studentRepository.findById(id);
        if (optional.isPresent()) {
            studentRepository.deleteById(id);
            addressRepository.deleteById(optional.get().getAddress().getId());
            return "O'chirildi";
        } else {
            return "Bunday Student topilmadi !!!";
        }
    }
}
