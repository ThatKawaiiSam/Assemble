package io.github.thatkawaiisam.assemble;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class AssembleTest {

    @Test
    @DisplayName("Simple 16 character text split")
    public void simpleTextSplit() {
        String[] split = AssembleUtils.splitTeamText("§6Howdy!");
        assertEquals("§6Howdy!", split[0]);
        assertEquals("", split[1]);
    }

    @Test
    @DisplayName("Full 32 character text split")
    public void fullTextSplit() {
        String[] split = AssembleUtils.splitTeamText("§6Howdy this is §emy server!");
        assertEquals("§6Howdy this is ", split[0]);
        assertEquals("§6§emy server!", split[1]);
    }

    @Test
    @DisplayName("Advanced text split")
    public void advancedTextSplit() {
        String[] split = AssembleUtils.splitTeamText("§eWow this is §6§lsome bold text!");
        assertEquals("§eWow this is ", split[0]);
        assertEquals("§6§lsome bold te", split[1]);
    }

}
