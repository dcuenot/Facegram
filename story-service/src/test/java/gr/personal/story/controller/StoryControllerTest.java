package gr.personal.story.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.security.auth.UserPrincipal;
import gr.personal.story.domain.*;
import gr.personal.story.service.StoryService;
import gr.personal.story.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static gr.personal.story.helper.FakeDataGenerator.generateComment;
import static gr.personal.story.helper.FakeDataGenerator.generateStory;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nick Kanakis on 14/5/2017.
 */

@RunWith(SpringRunner.class)
@ActiveProfiles("noEureka")
public class StoryControllerTest {
    @MockBean
    private StoryService storyService;
    private MockMvc mockMvc;
    @InjectMocks
    private StoryController storyController;

    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(storyController).build();
    }

    @Test
    public void shouldFetchStory() throws Exception {
        Story story = generateStory();

        when(storyService.fetchStory(anyString())).thenReturn(story);

        mockMvc.perform(get("/story/fetch/test"))
                .andExpect(jsonPath("$.id").value(story.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateStory() throws Exception {
        StoryRequest storyRequest = new StoryRequest("test","test","1",new Geolocation(0.0,0.0));

        when(storyService.createStory(anyString(), any(StoryRequest.class))).thenReturn(Constants.OK);

        mockMvc.perform(post("/story/create")
                    .content(asJsonString(storyRequest))
                    .contentType(MediaType.APPLICATION_JSON).principal(new UserPrincipal("testUserId")))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> Constants.OK.equals(mvcResult));

    }

    @Test
    public void shouldDeleteStory() throws Exception {
        when(storyService.deleteStory(anyString(), anyString())).thenReturn(Constants.OK);

        mockMvc.perform(delete("/story/delete/test").principal(new UserPrincipal("testUserId")))
                .andExpect(mvcResult -> Constants.OK.equals(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldLikeStory() throws Exception {
        when(storyService.likeStory(anyString())).thenReturn(Constants.OK);

        mockMvc.perform(post("/story/like/test"))
                .andExpect(mvcResult -> Constants.OK.equals(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUnLikeStory() throws Exception {
        when(storyService.unlikeStory(anyString())).thenReturn(Constants.OK);

        mockMvc.perform(post("/story/unlike/test"))
                .andExpect(mvcResult -> Constants.OK.equals(mvcResult))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateComment() throws Exception{
        CommentRequest comment = new CommentRequest("testHeader","test");

        when(storyService.createComment(eq("testUserId"), anyString(), any(CommentRequest.class))).thenReturn(Constants.OK);

        mockMvc.perform(post("/story/comment/testStoryId").contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(comment)).principal(new UserPrincipal("testUserId")))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> Constants.OK.equals(mvcResult));
    }

    @Test
    public void shouldDeleteComment() throws Exception{
        when(storyService.deleteComment(anyString(), anyString())).thenReturn(Constants.OK);

        mockMvc.perform(delete("/story/uncomment/testCommentId").principal(new UserPrincipal("testUserId")))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> Constants.OK.equals(mvcResult));
    }

    @Test
    public void shouldFetchComment() throws Exception{
        Comment comment = generateComment();

        when(storyService.fetchComment(anyString())).thenReturn(comment);

        mockMvc.perform(get("/story/fetchComment/testCommentId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.getId()));
    }

    private String asJsonString(Object input) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(input);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
