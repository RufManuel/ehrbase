package org.ehrbase.rest.openehr;

import com.nedap.archie.rm.composition.Composition;
import org.ehrbase.api.definitions.ServerConfig;
import org.ehrbase.api.service.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.ehrbase.rest.RestModuleConfiguration;
import org.ehrbase.rest.StatusController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@WithMockUser
@ContextConfiguration(classes = RestModuleConfiguration.class)
@AutoConfigureMockMvc
public class RestControllerTest {

    @MockBean
    private CompositionService compositionService;
    @MockBean
    private StatusController statusController;
    @MockBean
    private FolderService folderService;
    @MockBean
    private ContributionService contributionService;
    @MockBean
    private QueryService queryService;
    @MockBean
    private EhrService ehrService;
    @MockBean
    private TemplateService templateService;

    @Mock
    private Composition compositionObject;
    @Mock
    private ServerConfig serverConfig;

    @Autowired
    @SuppressWarnings("")
    private MockMvc mockMvc;

    private final UUID ehrUuid = UUID.randomUUID();
    private final UUID compositionUuid = UUID.randomUUID();
    private final String compositionCollection = "http://localhost/rest/openehr/v1/ehr/" + ehrUuid + "/composition";
    private final String compositionEntity = compositionCollection + "/" + compositionUuid;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLocationHeaderValueForPost() throws Exception {
        given(this.compositionService.buildComposition(anyString(), any(), any()))
                .willReturn(compositionObject);

        given(this.compositionService.create(ehrUuid, compositionObject))
                .willReturn(Optional.ofNullable(compositionUuid));

        given(this.compositionService.getServerConfig())
                .willReturn(serverConfig);

        given(this.serverConfig.getNodename())
                .willReturn("someNodeName");

        mockMvc.perform(post(compositionCollection)
                        .with(csrf())
                        .content("someContent")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isNoContent(),
                        header().string("Location", compositionEntity))
                .andReturn();
    }
}