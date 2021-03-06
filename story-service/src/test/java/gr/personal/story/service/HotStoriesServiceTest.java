package gr.personal.story.service;

import gr.personal.story.domain.Geolocation;
import gr.personal.story.domain.Story;
import gr.personal.story.repository.StoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static gr.personal.story.helper.FakeDataGenerator.generateStories;
import static gr.personal.story.helper.FakeDataGenerator.getRandomGeoLocation;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created by Nick Kanakis on 14/5/2017.
 */

@RunWith(SpringRunner.class)
public class HotStoriesServiceTest {
    @MockBean
    private StoryRepository storyRepository;
    private List<Story> originalStories;
    @Autowired
    private StoriesService hotStoriesService;

    @TestConfiguration
    static class StoriesServiceTestContextConfiguration {
        @Bean
        public StoriesService hotStoriesService() {
            return new HotStoriesService();
        }
        @Bean
        public CacheManager serviceCacheManager(){
            return new ConcurrentMapCacheManager("testCache");
        }
    }

    @Before
    public void setup(){
        originalStories = generateStories();

        Mockito.when(storyRepository.findHotStoriesOfGroup(anyString())).thenReturn(originalStories);
        Mockito.when(storyRepository.findHotStoriesOfUser(anyString())).thenReturn(originalStories);
        Mockito.when(storyRepository.findHotStoriesOfLocation(any(Geolocation.class))).thenReturn(originalStories);
    }

    @Test
    public void shouldGetHotStoriesOfGroup() throws Exception {
        List<Story> stories = hotStoriesService.getStoriesOfGroup("testGroupId");
        assertThat(stories,is(originalStories));
    }

    @Test
    public void shouldGetHotStoriesOfUser() throws Exception {
        List<Story> stories = hotStoriesService.getStoriesOfUser("testUserId");
        assertThat(stories,is(originalStories));
    }

    @Test
    public void shouldGetHotStoriesOfLocation() throws Exception {
        List<Story> stories = hotStoriesService.getStoriesOfLocation(getRandomGeoLocation());
        assertThat(stories,is(originalStories));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToGetHotStoriesOfGroup(){
        hotStoriesService.getStoriesOfGroup("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToGetHotStoriesOfLocation(){
        hotStoriesService.getStoriesOfLocation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToGetHotStoriesOfUser(){
        hotStoriesService.getStoriesOfUser("");
    }
}
