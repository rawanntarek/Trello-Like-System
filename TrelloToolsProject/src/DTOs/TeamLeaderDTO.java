package DTOs;

import java.util.List;

public class TeamLeaderDTO extends UserDTO {
    private List<BoardDTO> boards;

    public List<BoardDTO> getBoards() {
        return boards;
    }

    public void setBoards(List<BoardDTO> boards) {
        this.boards = boards;
    }
}