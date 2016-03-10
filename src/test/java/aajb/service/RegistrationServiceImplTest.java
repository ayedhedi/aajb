package aajb.service;

import aajb.Application;
import aajb.domain.school.Parent;
import aajb.domain.school.Student;
import aajb.service.dto.ChequeDto;
import aajb.service.dto.ParentDto;
import aajb.service.dto.RegistrationDto;
import aajb.service.dto.StudentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by ayed.h on 01/03/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class RegistrationServiceImplTest {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    RegistrationService registrationService;
    @Qualifier("parentService")
    @Autowired
    private ParentService parentService;
    @Qualifier("studentServiceImpl")
    @Autowired
    private StudentService studentService;


    @Test
    @Transactional(rollbackFor = {Exception.class})
    public void testCreateRegistrationWithExistingParents() throws Exception {
        ParentDto pDto = new ParentDto();
        pDto.setFirstName("firstp");
        pDto.setLastName("lastp");
        pDto.setCaf(5454);
        pDto.setEmail("p1@gmail.com");
        pDto.setGender("MALE");
        pDto.setSsn("545425454");
        pDto.setTel("DFZ25425425425");
        Parent p1 = parentService.createParent(ParentDto.asParent(pDto));
        Assert.notNull(p1);
        Assert.notNull(p1.getId());

        pDto = new ParentDto();
        pDto.setFirstName("secondp");
        pDto.setLastName("lastp");
        pDto.setCaf(410);
        pDto.setEmail("p2@gmail.com");
        pDto.setGender("FEMALE");
        pDto.setSsn("65145154");
        pDto.setTel("54154154154");
        Parent p2 = parentService.createParent(ParentDto.asParent(pDto));
        Assert.notNull(p2);
        Assert.notNull(p2.getId());

        StudentDto sDto = new StudentDto();
        sDto.setClassName("GA");
        sDto.setFirstName("firsts");
        sDto.setLastName("lastsp");
        sDto.setBirthDate(sdf.parse("21/11/2010"));
        sDto.setGender("MALE");
        sDto.setRemarks("Droit Image");
        Student s1 = StudentDto.asStudent(sDto);
        s1.setFirstParent(p1);
        s1.setSecondParent(p2);
        s1 = studentService.createStudent(s1);
        Assert.notNull(s1);
        Assert.notNull(s1.getId());

        sDto = new StudentDto();
        sDto.setClassName("BA");
        sDto.setFirstName("sdfsdfsddf");
        sDto.setLastName("reyery");
        sDto.setBirthDate(sdf.parse("11/11/2009"));
        sDto.setGender("FEMALE");
        Student s2 = StudentDto.asStudent(sDto);
        s2.setFirstParent(p1);
        s2.setSecondParent(p2);
        s2 = studentService.createStudent(s2);
        Assert.notNull(s2);
        Assert.notNull(s2.getId());

        ChequeDto chequeDto1 = new ChequeDto();
        chequeDto1.setAdjustableDate(new Date());
        chequeDto1.setRemarks("No remarks");
        chequeDto1.setAmount(124);
        chequeDto1.setBankName("BA");
        chequeDto1.setNumber("122sd2552sd");

        ChequeDto chequeDto2 = new ChequeDto();
        chequeDto2.setAdjustableDate(new Date());
        chequeDto2.setRemarks("No remarks");
        chequeDto2.setAmount(584786);

        RegistrationDto registrationDto = new RegistrationDto();
        ParentDto p1Dto = new ParentDto();
        p1Dto.setId(p1.getId());
        ParentDto p2Dto = new ParentDto();
        p2Dto.setId(p2.getId());
        StudentDto s1Dto = new StudentDto();
        s1Dto.setId(s1.getId());
        StudentDto s2Dto = new StudentDto();
        s2Dto.setId(s2.getId());
        registrationDto.setStudents(new HashSet<>());
        registrationDto.getStudents().add(s1Dto);
        registrationDto.getStudents().add(s2Dto);
        registrationDto.setFirstParent(p1Dto);
        registrationDto.setSecondParent(p2Dto);
        registrationDto.setCheques(new HashSet<>());
        registrationDto.getCheques().add(chequeDto1);
        registrationDto.getCheques().add(chequeDto2);

        RegistrationDto registration = registrationService.createRegistration(registrationDto);
        Assert.notNull(registration);
        Assert.notNull(registration.getId());

        //remove all !!
        s1 = studentService.deleteStudent(s1.getId());
        Assert.notNull(s1);
        Assert.isTrue(!s1.getActive());

        s2 = studentService.deleteStudent(s2.getId());
        Assert.notNull(s2);
        Assert.isTrue(!s2.getActive());

        p1 = parentService.deleteParent(p1.getId());
        Assert.notNull(p1);
        Assert.isTrue(!p1.getActive());

        p2 = parentService.deleteParent(p2.getId());
        Assert.notNull(p2);
        Assert.isTrue(!p2.getActive());

        registrationService.deleteRegistration(registration.getId());
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testCreateRegistrationFromScratch() throws Exception {
        ParentDto p1Dto = new ParentDto();
        p1Dto.setFirstName("firstp");
        p1Dto.setLastName("lastp");
        p1Dto.setCaf(5454);
        p1Dto.setEmail("p1@gmail.com");
        p1Dto.setGender("MALE");
        p1Dto.setSsn("545425454");
        p1Dto.setTel("DFZ25425425425");
        p1Dto.setAddress("A place machin France 70258");

        ParentDto p2Dto = new ParentDto();
        p2Dto.setFirstName("secondp");
        p2Dto.setLastName("lastp");
        p2Dto.setCaf(410);
        p2Dto.setEmail("p2@gmail.com");
        p2Dto.setGender("FEMALE");
        p2Dto.setSsn("65145154");
        p2Dto.setTel("54154154154");

        StudentDto s1Dto = new StudentDto();
        s1Dto.setClassName("GA");
        s1Dto.setFirstName("firsts");
        s1Dto.setLastName("lastsp");
        s1Dto.setBirthDate(sdf.parse("21/11/2010"));
        s1Dto.setGender("MALE");
        s1Dto.setRemarks("Droit Image");

        StudentDto s2Dto = new StudentDto();
        s2Dto.setClassName("BA");
        s2Dto.setFirstName("sdfsdfsddf");
        s2Dto.setLastName("reyery");
        s2Dto.setBirthDate(sdf.parse("11/11/2009"));
        s2Dto.setGender("FEMALE");

        ChequeDto chequeDto1 = new ChequeDto();
        chequeDto1.setAdjustableDate(new Date());
        chequeDto1.setRemarks("No remarks");
        chequeDto1.setAmount(124);
        chequeDto1.setBankName("BA");
        chequeDto1.setNumber("122sd2552sd");

        ChequeDto chequeDto2 = new ChequeDto();
        chequeDto2.setAdjustableDate(new Date());
        chequeDto2.setRemarks("No remarks");
        chequeDto2.setAmount(584786);

        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setStudents(new HashSet<>());
        registrationDto.getStudents().add(s1Dto);
        registrationDto.getStudents().add(s2Dto);
        registrationDto.setFirstParent(p1Dto);
        registrationDto.setSecondParent(p2Dto);
        registrationDto.setCheques(new HashSet<>());
        registrationDto.getCheques().add(chequeDto1);
        registrationDto.getCheques().add(chequeDto2);

        StringWriter sw = new StringWriter();
        (new ObjectMapper()).writeValue(sw,registrationDto);
        System.out.println(sw.toString());

        registrationDto = registrationService.createRegistration(registrationDto);
        Assert.notNull(registrationDto);
        Assert.notNull(registrationDto.getId());
    }
}