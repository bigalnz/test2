package com.geekhelp.test.web.rest;

import com.geekhelp.test.Test2App;
import com.geekhelp.test.domain.Person;
import com.geekhelp.test.repository.PersonRepository;
import com.geekhelp.test.service.PersonService;
import com.geekhelp.test.service.dto.PersonCriteria;
import com.geekhelp.test.service.PersonQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PersonResource} REST controller.
 */
@SpringBootTest(classes = Test2App.class)

@AutoConfigureMockMvc
@WithMockUser
public class PersonResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DOB = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_HOTEL = "AAAAAAAAAA";
    private static final String UPDATED_HOTEL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATEIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEIN = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATEOUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEOUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATEOUT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonQueryService personQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonMockMvc;

    private Person person;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createEntity(EntityManager em) {
        Person person = new Person()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .dob(DEFAULT_DOB)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .email(DEFAULT_EMAIL)
            .hotel(DEFAULT_HOTEL)
            .datein(DEFAULT_DATEIN)
            .dateout(DEFAULT_DATEOUT)
            .comments(DEFAULT_COMMENTS);
        return person;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createUpdatedEntity(EntityManager em) {
        Person person = new Person()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .dob(UPDATED_DOB)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .hotel(UPDATED_HOTEL)
            .datein(UPDATED_DATEIN)
            .dateout(UPDATED_DATEOUT)
            .comments(UPDATED_COMMENTS);
        return person;
    }

    @BeforeEach
    public void initTest() {
        person = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person
        restPersonMockMvc.perform(post("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testPerson.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testPerson.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testPerson.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPerson.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPerson.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPerson.getHotel()).isEqualTo(DEFAULT_HOTEL);
        assertThat(testPerson.getDatein()).isEqualTo(DEFAULT_DATEIN);
        assertThat(testPerson.getDateout()).isEqualTo(DEFAULT_DATEOUT);
        assertThat(testPerson.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person with an existing ID
        person.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc.perform(post("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setFirstname(null);

        // Create the Person, which fails.

        restPersonMockMvc.perform(post("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setLastname(null);

        // Create the Person, which fails.

        restPersonMockMvc.perform(post("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList
        restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hotel").value(hasItem(DEFAULT_HOTEL)))
            .andExpect(jsonPath("$.[*].datein").value(hasItem(DEFAULT_DATEIN.toString())))
            .andExpect(jsonPath("$.[*].dateout").value(hasItem(DEFAULT_DATEOUT.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }
    
    @Test
    @Transactional
    public void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hotel").value(DEFAULT_HOTEL))
            .andExpect(jsonPath("$.datein").value(DEFAULT_DATEIN.toString()))
            .andExpect(jsonPath("$.dateout").value(DEFAULT_DATEOUT.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }


    @Test
    @Transactional
    public void getPeopleByIdFiltering() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        Long id = person.getId();

        defaultPersonShouldBeFound("id.equals=" + id);
        defaultPersonShouldNotBeFound("id.notEquals=" + id);

        defaultPersonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPeopleByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstname equals to DEFAULT_FIRSTNAME
        defaultPersonShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the personList where firstname equals to UPDATED_FIRSTNAME
        defaultPersonShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByFirstnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstname not equals to DEFAULT_FIRSTNAME
        defaultPersonShouldNotBeFound("firstname.notEquals=" + DEFAULT_FIRSTNAME);

        // Get all the personList where firstname not equals to UPDATED_FIRSTNAME
        defaultPersonShouldBeFound("firstname.notEquals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultPersonShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the personList where firstname equals to UPDATED_FIRSTNAME
        defaultPersonShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstname is not null
        defaultPersonShouldBeFound("firstname.specified=true");

        // Get all the personList where firstname is null
        defaultPersonShouldNotBeFound("firstname.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstname contains DEFAULT_FIRSTNAME
        defaultPersonShouldBeFound("firstname.contains=" + DEFAULT_FIRSTNAME);

        // Get all the personList where firstname contains UPDATED_FIRSTNAME
        defaultPersonShouldNotBeFound("firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstname does not contain DEFAULT_FIRSTNAME
        defaultPersonShouldNotBeFound("firstname.doesNotContain=" + DEFAULT_FIRSTNAME);

        // Get all the personList where firstname does not contain UPDATED_FIRSTNAME
        defaultPersonShouldBeFound("firstname.doesNotContain=" + UPDATED_FIRSTNAME);
    }


    @Test
    @Transactional
    public void getAllPeopleByLastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastname equals to DEFAULT_LASTNAME
        defaultPersonShouldBeFound("lastname.equals=" + DEFAULT_LASTNAME);

        // Get all the personList where lastname equals to UPDATED_LASTNAME
        defaultPersonShouldNotBeFound("lastname.equals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastname not equals to DEFAULT_LASTNAME
        defaultPersonShouldNotBeFound("lastname.notEquals=" + DEFAULT_LASTNAME);

        // Get all the personList where lastname not equals to UPDATED_LASTNAME
        defaultPersonShouldBeFound("lastname.notEquals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastnameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastname in DEFAULT_LASTNAME or UPDATED_LASTNAME
        defaultPersonShouldBeFound("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME);

        // Get all the personList where lastname equals to UPDATED_LASTNAME
        defaultPersonShouldNotBeFound("lastname.in=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastname is not null
        defaultPersonShouldBeFound("lastname.specified=true");

        // Get all the personList where lastname is null
        defaultPersonShouldNotBeFound("lastname.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByLastnameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastname contains DEFAULT_LASTNAME
        defaultPersonShouldBeFound("lastname.contains=" + DEFAULT_LASTNAME);

        // Get all the personList where lastname contains UPDATED_LASTNAME
        defaultPersonShouldNotBeFound("lastname.contains=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void getAllPeopleByLastnameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastname does not contain DEFAULT_LASTNAME
        defaultPersonShouldNotBeFound("lastname.doesNotContain=" + DEFAULT_LASTNAME);

        // Get all the personList where lastname does not contain UPDATED_LASTNAME
        defaultPersonShouldBeFound("lastname.doesNotContain=" + UPDATED_LASTNAME);
    }


    @Test
    @Transactional
    public void getAllPeopleByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dob equals to DEFAULT_DOB
        defaultPersonShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the personList where dob equals to UPDATED_DOB
        defaultPersonShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    public void getAllPeopleByDobIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dob not equals to DEFAULT_DOB
        defaultPersonShouldNotBeFound("dob.notEquals=" + DEFAULT_DOB);

        // Get all the personList where dob not equals to UPDATED_DOB
        defaultPersonShouldBeFound("dob.notEquals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    public void getAllPeopleByDobIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultPersonShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the personList where dob equals to UPDATED_DOB
        defaultPersonShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    public void getAllPeopleByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dob is not null
        defaultPersonShouldBeFound("dob.specified=true");

        // Get all the personList where dob is null
        defaultPersonShouldNotBeFound("dob.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dob is greater than or equal to DEFAULT_DOB
        defaultPersonShouldBeFound("dob.greaterThanOrEqual=" + DEFAULT_DOB);

        // Get all the personList where dob is greater than or equal to UPDATED_DOB
        defaultPersonShouldNotBeFound("dob.greaterThanOrEqual=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    public void getAllPeopleByDobIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dob is less than or equal to DEFAULT_DOB
        defaultPersonShouldBeFound("dob.lessThanOrEqual=" + DEFAULT_DOB);

        // Get all the personList where dob is less than or equal to SMALLER_DOB
        defaultPersonShouldNotBeFound("dob.lessThanOrEqual=" + SMALLER_DOB);
    }

    @Test
    @Transactional
    public void getAllPeopleByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dob is less than DEFAULT_DOB
        defaultPersonShouldNotBeFound("dob.lessThan=" + DEFAULT_DOB);

        // Get all the personList where dob is less than UPDATED_DOB
        defaultPersonShouldBeFound("dob.lessThan=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    public void getAllPeopleByDobIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dob is greater than DEFAULT_DOB
        defaultPersonShouldNotBeFound("dob.greaterThan=" + DEFAULT_DOB);

        // Get all the personList where dob is greater than SMALLER_DOB
        defaultPersonShouldBeFound("dob.greaterThan=" + SMALLER_DOB);
    }


    @Test
    @Transactional
    public void getAllPeopleByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone equals to DEFAULT_PHONE
        defaultPersonShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the personList where phone equals to UPDATED_PHONE
        defaultPersonShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone not equals to DEFAULT_PHONE
        defaultPersonShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the personList where phone not equals to UPDATED_PHONE
        defaultPersonShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultPersonShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the personList where phone equals to UPDATED_PHONE
        defaultPersonShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone is not null
        defaultPersonShouldBeFound("phone.specified=true");

        // Get all the personList where phone is null
        defaultPersonShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByPhoneContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone contains DEFAULT_PHONE
        defaultPersonShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the personList where phone contains UPDATED_PHONE
        defaultPersonShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPeopleByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where phone does not contain DEFAULT_PHONE
        defaultPersonShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the personList where phone does not contain UPDATED_PHONE
        defaultPersonShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllPeopleByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where address equals to DEFAULT_ADDRESS
        defaultPersonShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the personList where address equals to UPDATED_ADDRESS
        defaultPersonShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPeopleByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where address not equals to DEFAULT_ADDRESS
        defaultPersonShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the personList where address not equals to UPDATED_ADDRESS
        defaultPersonShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPeopleByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultPersonShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the personList where address equals to UPDATED_ADDRESS
        defaultPersonShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPeopleByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where address is not null
        defaultPersonShouldBeFound("address.specified=true");

        // Get all the personList where address is null
        defaultPersonShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByAddressContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where address contains DEFAULT_ADDRESS
        defaultPersonShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the personList where address contains UPDATED_ADDRESS
        defaultPersonShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPeopleByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where address does not contain DEFAULT_ADDRESS
        defaultPersonShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the personList where address does not contain UPDATED_ADDRESS
        defaultPersonShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllPeopleByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where email equals to DEFAULT_EMAIL
        defaultPersonShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the personList where email equals to UPDATED_EMAIL
        defaultPersonShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where email not equals to DEFAULT_EMAIL
        defaultPersonShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the personList where email not equals to UPDATED_EMAIL
        defaultPersonShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPersonShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the personList where email equals to UPDATED_EMAIL
        defaultPersonShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where email is not null
        defaultPersonShouldBeFound("email.specified=true");

        // Get all the personList where email is null
        defaultPersonShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByEmailContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where email contains DEFAULT_EMAIL
        defaultPersonShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the personList where email contains UPDATED_EMAIL
        defaultPersonShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPeopleByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where email does not contain DEFAULT_EMAIL
        defaultPersonShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the personList where email does not contain UPDATED_EMAIL
        defaultPersonShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllPeopleByHotelIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where hotel equals to DEFAULT_HOTEL
        defaultPersonShouldBeFound("hotel.equals=" + DEFAULT_HOTEL);

        // Get all the personList where hotel equals to UPDATED_HOTEL
        defaultPersonShouldNotBeFound("hotel.equals=" + UPDATED_HOTEL);
    }

    @Test
    @Transactional
    public void getAllPeopleByHotelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where hotel not equals to DEFAULT_HOTEL
        defaultPersonShouldNotBeFound("hotel.notEquals=" + DEFAULT_HOTEL);

        // Get all the personList where hotel not equals to UPDATED_HOTEL
        defaultPersonShouldBeFound("hotel.notEquals=" + UPDATED_HOTEL);
    }

    @Test
    @Transactional
    public void getAllPeopleByHotelIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where hotel in DEFAULT_HOTEL or UPDATED_HOTEL
        defaultPersonShouldBeFound("hotel.in=" + DEFAULT_HOTEL + "," + UPDATED_HOTEL);

        // Get all the personList where hotel equals to UPDATED_HOTEL
        defaultPersonShouldNotBeFound("hotel.in=" + UPDATED_HOTEL);
    }

    @Test
    @Transactional
    public void getAllPeopleByHotelIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where hotel is not null
        defaultPersonShouldBeFound("hotel.specified=true");

        // Get all the personList where hotel is null
        defaultPersonShouldNotBeFound("hotel.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByHotelContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where hotel contains DEFAULT_HOTEL
        defaultPersonShouldBeFound("hotel.contains=" + DEFAULT_HOTEL);

        // Get all the personList where hotel contains UPDATED_HOTEL
        defaultPersonShouldNotBeFound("hotel.contains=" + UPDATED_HOTEL);
    }

    @Test
    @Transactional
    public void getAllPeopleByHotelNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where hotel does not contain DEFAULT_HOTEL
        defaultPersonShouldNotBeFound("hotel.doesNotContain=" + DEFAULT_HOTEL);

        // Get all the personList where hotel does not contain UPDATED_HOTEL
        defaultPersonShouldBeFound("hotel.doesNotContain=" + UPDATED_HOTEL);
    }


    @Test
    @Transactional
    public void getAllPeopleByDateinIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where datein equals to DEFAULT_DATEIN
        defaultPersonShouldBeFound("datein.equals=" + DEFAULT_DATEIN);

        // Get all the personList where datein equals to UPDATED_DATEIN
        defaultPersonShouldNotBeFound("datein.equals=" + UPDATED_DATEIN);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where datein not equals to DEFAULT_DATEIN
        defaultPersonShouldNotBeFound("datein.notEquals=" + DEFAULT_DATEIN);

        // Get all the personList where datein not equals to UPDATED_DATEIN
        defaultPersonShouldBeFound("datein.notEquals=" + UPDATED_DATEIN);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateinIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where datein in DEFAULT_DATEIN or UPDATED_DATEIN
        defaultPersonShouldBeFound("datein.in=" + DEFAULT_DATEIN + "," + UPDATED_DATEIN);

        // Get all the personList where datein equals to UPDATED_DATEIN
        defaultPersonShouldNotBeFound("datein.in=" + UPDATED_DATEIN);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateinIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where datein is not null
        defaultPersonShouldBeFound("datein.specified=true");

        // Get all the personList where datein is null
        defaultPersonShouldNotBeFound("datein.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByDateinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where datein is greater than or equal to DEFAULT_DATEIN
        defaultPersonShouldBeFound("datein.greaterThanOrEqual=" + DEFAULT_DATEIN);

        // Get all the personList where datein is greater than or equal to UPDATED_DATEIN
        defaultPersonShouldNotBeFound("datein.greaterThanOrEqual=" + UPDATED_DATEIN);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where datein is less than or equal to DEFAULT_DATEIN
        defaultPersonShouldBeFound("datein.lessThanOrEqual=" + DEFAULT_DATEIN);

        // Get all the personList where datein is less than or equal to SMALLER_DATEIN
        defaultPersonShouldNotBeFound("datein.lessThanOrEqual=" + SMALLER_DATEIN);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateinIsLessThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where datein is less than DEFAULT_DATEIN
        defaultPersonShouldNotBeFound("datein.lessThan=" + DEFAULT_DATEIN);

        // Get all the personList where datein is less than UPDATED_DATEIN
        defaultPersonShouldBeFound("datein.lessThan=" + UPDATED_DATEIN);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where datein is greater than DEFAULT_DATEIN
        defaultPersonShouldNotBeFound("datein.greaterThan=" + DEFAULT_DATEIN);

        // Get all the personList where datein is greater than SMALLER_DATEIN
        defaultPersonShouldBeFound("datein.greaterThan=" + SMALLER_DATEIN);
    }


    @Test
    @Transactional
    public void getAllPeopleByDateoutIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateout equals to DEFAULT_DATEOUT
        defaultPersonShouldBeFound("dateout.equals=" + DEFAULT_DATEOUT);

        // Get all the personList where dateout equals to UPDATED_DATEOUT
        defaultPersonShouldNotBeFound("dateout.equals=" + UPDATED_DATEOUT);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateoutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateout not equals to DEFAULT_DATEOUT
        defaultPersonShouldNotBeFound("dateout.notEquals=" + DEFAULT_DATEOUT);

        // Get all the personList where dateout not equals to UPDATED_DATEOUT
        defaultPersonShouldBeFound("dateout.notEquals=" + UPDATED_DATEOUT);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateoutIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateout in DEFAULT_DATEOUT or UPDATED_DATEOUT
        defaultPersonShouldBeFound("dateout.in=" + DEFAULT_DATEOUT + "," + UPDATED_DATEOUT);

        // Get all the personList where dateout equals to UPDATED_DATEOUT
        defaultPersonShouldNotBeFound("dateout.in=" + UPDATED_DATEOUT);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateoutIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateout is not null
        defaultPersonShouldBeFound("dateout.specified=true");

        // Get all the personList where dateout is null
        defaultPersonShouldNotBeFound("dateout.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByDateoutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateout is greater than or equal to DEFAULT_DATEOUT
        defaultPersonShouldBeFound("dateout.greaterThanOrEqual=" + DEFAULT_DATEOUT);

        // Get all the personList where dateout is greater than or equal to UPDATED_DATEOUT
        defaultPersonShouldNotBeFound("dateout.greaterThanOrEqual=" + UPDATED_DATEOUT);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateoutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateout is less than or equal to DEFAULT_DATEOUT
        defaultPersonShouldBeFound("dateout.lessThanOrEqual=" + DEFAULT_DATEOUT);

        // Get all the personList where dateout is less than or equal to SMALLER_DATEOUT
        defaultPersonShouldNotBeFound("dateout.lessThanOrEqual=" + SMALLER_DATEOUT);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateoutIsLessThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateout is less than DEFAULT_DATEOUT
        defaultPersonShouldNotBeFound("dateout.lessThan=" + DEFAULT_DATEOUT);

        // Get all the personList where dateout is less than UPDATED_DATEOUT
        defaultPersonShouldBeFound("dateout.lessThan=" + UPDATED_DATEOUT);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateoutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateout is greater than DEFAULT_DATEOUT
        defaultPersonShouldNotBeFound("dateout.greaterThan=" + DEFAULT_DATEOUT);

        // Get all the personList where dateout is greater than SMALLER_DATEOUT
        defaultPersonShouldBeFound("dateout.greaterThan=" + SMALLER_DATEOUT);
    }


    @Test
    @Transactional
    public void getAllPeopleByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where comments equals to DEFAULT_COMMENTS
        defaultPersonShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the personList where comments equals to UPDATED_COMMENTS
        defaultPersonShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPeopleByCommentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where comments not equals to DEFAULT_COMMENTS
        defaultPersonShouldNotBeFound("comments.notEquals=" + DEFAULT_COMMENTS);

        // Get all the personList where comments not equals to UPDATED_COMMENTS
        defaultPersonShouldBeFound("comments.notEquals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPeopleByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultPersonShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the personList where comments equals to UPDATED_COMMENTS
        defaultPersonShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPeopleByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where comments is not null
        defaultPersonShouldBeFound("comments.specified=true");

        // Get all the personList where comments is null
        defaultPersonShouldNotBeFound("comments.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByCommentsContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where comments contains DEFAULT_COMMENTS
        defaultPersonShouldBeFound("comments.contains=" + DEFAULT_COMMENTS);

        // Get all the personList where comments contains UPDATED_COMMENTS
        defaultPersonShouldNotBeFound("comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPeopleByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where comments does not contain DEFAULT_COMMENTS
        defaultPersonShouldNotBeFound("comments.doesNotContain=" + DEFAULT_COMMENTS);

        // Get all the personList where comments does not contain UPDATED_COMMENTS
        defaultPersonShouldBeFound("comments.doesNotContain=" + UPDATED_COMMENTS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonShouldBeFound(String filter) throws Exception {
        restPersonMockMvc.perform(get("/api/people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hotel").value(hasItem(DEFAULT_HOTEL)))
            .andExpect(jsonPath("$.[*].datein").value(hasItem(DEFAULT_DATEIN.toString())))
            .andExpect(jsonPath("$.[*].dateout").value(hasItem(DEFAULT_DATEOUT.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));

        // Check, that the count call also returns 1
        restPersonMockMvc.perform(get("/api/people/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonShouldNotBeFound(String filter) throws Exception {
        restPersonMockMvc.perform(get("/api/people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonMockMvc.perform(get("/api/people/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerson() throws Exception {
        // Initialize the database
        personService.save(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = personRepository.findById(person.getId()).get();
        // Disconnect from session so that the updates on updatedPerson are not directly saved in db
        em.detach(updatedPerson);
        updatedPerson
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .dob(UPDATED_DOB)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .hotel(UPDATED_HOTEL)
            .datein(UPDATED_DATEIN)
            .dateout(UPDATED_DATEOUT)
            .comments(UPDATED_COMMENTS);

        restPersonMockMvc.perform(put("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPerson)))
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testPerson.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testPerson.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testPerson.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPerson.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPerson.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPerson.getHotel()).isEqualTo(UPDATED_HOTEL);
        assertThat(testPerson.getDatein()).isEqualTo(UPDATED_DATEIN);
        assertThat(testPerson.getDateout()).isEqualTo(UPDATED_DATEOUT);
        assertThat(testPerson.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Create the Person

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc.perform(put("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePerson() throws Exception {
        // Initialize the database
        personService.save(person);

        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Delete the person
        restPersonMockMvc.perform(delete("/api/people/{id}", person.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
