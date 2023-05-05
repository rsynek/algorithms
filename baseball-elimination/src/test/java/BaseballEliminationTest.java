import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseballEliminationTest {

    private final static String DATA_FOLDER = "data";

    @Test
    public void testReadInput() {
        BaseballElimination baseballElimination = new BaseballElimination(getFilename("teams4.txt"));

        assertThat(baseballElimination.numberOfTeams()).isEqualTo(4);
        assertThat(baseballElimination.wins("Atlanta")).isEqualTo(83);
        assertThat(baseballElimination.losses("Philadelphia")).isEqualTo(79);
        assertThat(baseballElimination.remaining("New_York")).isEqualTo(6);
        assertThat(baseballElimination.against("Montreal", "Philadelphia")).isEqualTo(2);
    }

    @Test
    public void testEliminationOn4Teams() {
        BaseballElimination baseballElimination = new BaseballElimination(getFilename("teams4.txt"));

        assertThat(baseballElimination.isEliminated("Atlanta")).isFalse();
        assertThat(baseballElimination.isEliminated("Philadelphia")).isTrue();
        assertThat(baseballElimination.isEliminated("New_York")).isFalse();
        assertThat(baseballElimination.isEliminated("Montreal")).isTrue();

        assertThat(baseballElimination.certificateOfElimination("Montreal"))
                .containsOnly("Atlanta");

        assertThat(baseballElimination.certificateOfElimination("Philadelphia"))
                .containsOnly("Atlanta", "New_York");
    }

    @Test
    public void testNoEliminationOnTeam1() {
        BaseballElimination baseballElimination = new BaseballElimination(getFilename("teams1.txt"));

        assertThat(baseballElimination.isEliminated("Turing")).isFalse();
        assertThat(baseballElimination.certificateOfElimination("Turing")).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonExistingTeam() {
        BaseballElimination baseballElimination = new BaseballElimination(getFilename("teams4.txt"));

        baseballElimination.isEliminated("Rwanda");
    }

    private String getFilename(String name) {
        return DATA_FOLDER + "/" + name;
    }
}
