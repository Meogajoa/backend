package naegamaja_server.naegamaja.domain.game;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
public class GameStatus {
    int[][] board = new int[20][20];
    int currentX = 0, currentY = 0; // 현재 캐릭터 위치 (초기값은 (0, 0))

    @PostConstruct
    public void init() {
        setGameStatus3();
    }

    // 현재 위치가 벽이 아닌지 확인하는 메서드
    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= 20 || y < 0 || y >= 20) {
            return false; // 범위를 벗어난 경우
        }
        return board[x][y] != 0; // 벽이 아닌 경우만 이동 가능
    }

    // 이동 후 맵 업데이트
    private void updateMap(int newX, int newY) {
        // 이전 위치는 길로 변경 (벽이 아님)
        board[currentX][currentY] = 1;

        // 새로운 위치에 캐릭터 표시
        board[newX][newY] = 2;

        // 새로운 위치로 이동
        currentX = newX;
        currentY = newY;
    }

    // 위로 이동
    public void up() {
        int newX = currentX - 1;
        if (isValidMove(newX, currentY)) {
            updateMap(newX, currentY);
        }
    }

    // 아래로 이동
    public void down() {
        int newX = currentX + 1;
        if (isValidMove(newX, currentY)) {
            updateMap(newX, currentY);
        }
    }

    // 왼쪽으로 이동
    public void left() {
        int newY = currentY - 1;
        if (isValidMove(currentX, newY)) {
            updateMap(currentX, newY);
        }
    }

    // 오른쪽으로 이동
    public void right() {
        int newY = currentY + 1;
        if (isValidMove(currentX, newY)) {
            updateMap(currentX, newY);
        }
    }

    // 현재 위치를 반환
    public int[] getCurrentPosition() {
        return new int[]{currentX, currentY};
    }

    // 초기 맵 세팅 1
    public void setGameStatus1() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j] = 0;
            }
        }

        for (int i = 0; i < 20; i++) {
            board[i][0] = 1;
            board[i][19] = 1;
            board[0][i] = 1;
            board[19][i] = 1;
        }

        for (int i = 1; i < 19; i++) {
            board[i][i] = 1;
        }

        board[0][0] = 2; // 시작점
    }

    // 초기 맵 세팅 2
    public void setGameStatus2() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j] = 0;
            }
        }

        for (int i = 0; i < 20; i++) {
            board[0][i] = 1;
            board[i][19] = 1;
        }

        for (int i = 1; i < 19; i++) {
            board[i][i] = 1;
        }

        for (int i = 5; i < 15; i++) {
            board[10][i] = 1;
        }

        board[0][0] = 2; // 시작점
    }

    // 초기 맵 세팅 3 (복잡한 미로 형태)
    public void setGameStatus3() {
        // 모든 칸을 벽으로 초기화
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j] = 0;
            }
        }

        // 수직 경로
        for (int i = 0; i < 20; i++) {
            board[i][0] = 1;  // 첫 번째 열
            board[i][19] = 1; // 마지막 열
        }

        // 수평 경로
        for (int j = 0; j < 20; j++) {
            board[19][j] = 1; // 마지막 행
        }

        // 내부 미로 경로 설정
        board[1][1] = 1;
        board[2][1] = 1;
        board[3][1] = 1;
        board[4][1] = 1;
        board[5][1] = 1;

        board[5][2] = 1;
        board[5][3] = 1;
        board[5][4] = 1;
        board[5][5] = 1;

        board[6][5] = 1;
        board[7][5] = 1;
        board[8][5] = 1;
        board[9][5] = 1;

        board[9][6] = 1;
        board[9][7] = 1;
        board[9][8] = 1;
        board[9][9] = 1;

        board[10][9] = 1;
        board[11][9] = 1;
        board[12][9] = 1;
        board[13][9] = 1;

        board[13][10] = 1;
        board[13][11] = 1;
        board[13][12] = 1;
        board[13][13] = 1;

        board[14][13] = 1;
        board[15][13] = 1;
        board[16][13] = 1;
        board[17][13] = 1;

        board[17][14] = 1;
        board[17][15] = 1;
        board[17][16] = 1;
        board[17][17] = 1;

        board[18][17] = 1;
        board[19][17] = 1;
        board[19][18] = 1;
        board[19][19] = 1; // 도착점

        board[0][0] = 2; // 시작점
    }
}
