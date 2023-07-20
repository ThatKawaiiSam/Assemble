package io.github.thatkawaiisam.assemble;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AssembleTest {

    @Test
    @DisplayName("Simple 16 character text split")
    public void simpleTextSplit() {
        String[] split = AssembleUtils.splitTeamText("§6Howdy!");
        Assertions.assertEquals("§6Howdy!", split[1]);
        Assertions.assertEquals("", split[2]);
    }

    @Test
    @DisplayName("Full 32 character text split")
    public void fullTextSplit() {
        String[] split = AssembleUtils.splitTeamText("§6Howdy this is §emy server!");
        Assertions.assertEquals("§6Howdy this is ", split[0]);
        Assertions.assertEquals("§6§emy server!", split[1]);
    }

    @Test
    @DisplayName("Advanced text split")
    public void advancedTextSplit() {
        String[] split = AssembleUtils.splitTeamText("§eWow this is §6§lsome bold text!");
        Assertions.assertEquals("§eWow this is ", split[0]);
        Assertions.assertEquals("§6§lsome bold te", split[1]);
        Assertions.assertEquals("§6§lxt!", split[2]);
    }


}
