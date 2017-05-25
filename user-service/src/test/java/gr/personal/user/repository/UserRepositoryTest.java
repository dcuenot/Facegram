package gr.personal.user.repository;

import gr.personal.user.domain.Gender;
import gr.personal.user.domain.User;
import gr.personal.user.util.FakeDataGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static gr.personal.user.util.FakeDataGenerator.generateUser;

/**
 * Created by Nick Kanakis on 18/5/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User user;

    @Before
    public void setup(){
        user = generateUser();

        userRepository.save(user);
        Assert.assertNotNull(user.getUsername());
    }

    @After
    public void tearDown(){
        userRepository.delete(user.getUsername());
    }


    @Test
    public void shouldFetchUser() throws Throwable{
        User retrievedUser = userRepository.findByUsername(user.getUsername());
        Assert.assertNotNull(retrievedUser);
        Assert.assertEquals(user.getName(), retrievedUser.getName());
    }

    @Test
    public void shouldUpdateUser() throws Throwable{
        user.setName("Ilias");
        userRepository.save(user);
        User retrievedUser = userRepository.findByUsername(user.getUsername());
        Assert.assertNotNull(retrievedUser);
        Assert.assertEquals(user.getName(), retrievedUser.getName());
    }


    @Test
    public void shouldReturnNullUser() throws Throwable{
        User user = userRepository.findByUsername("notExistingUsername");
        Assert.assertNull(user);
    }

    @Test
    public void shouldReturnUserByUsername() throws Exception {
        User retrievedUser = userRepository.findByUsername(user.getUsername());
        Assert.assertNotNull(retrievedUser);
        Assert.assertEquals(retrievedUser.getUsername(),user.getUsername());

    }

}
