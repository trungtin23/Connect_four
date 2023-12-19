package Connect_4_Minimax;
import java.awt.Point;
import java.util.Stack;

public class GameState {
	private Boolean[][] pieces; // true = đỏ, false = vàng, null = không có quân cờ
	private Stack<Point> moves;
	private String error;
	private boolean gameOver;
	private boolean redWins;
	private boolean yellowWins;
	private boolean redsTurn;
	
	// Bắt đầu trò chơi với lượt của đỏ
	public GameState() {
		pieces = new Boolean[7][6];
		gameOver = false;
		redWins = false;
		yellowWins = false;
		redsTurn = true;
		moves = new Stack<>();
	}
	
	// Đặt lại thông báo lỗi và thực hiện nước đi tiếp theo vào hàng được chỉ định
	// Nếu nước đi không hợp lệ, đặt thông báo lỗi tương ứng
	public void move(int row) {
		error = null;
		// Không thể thực hiện nước đi nếu trò chơi đã kết thúc
		if(gameOver) setErrorMessage();
		// Không thể thực hiện nước đi nếu hàng đã đầy
		else if(pieces[row - 1][5] != null) setErrorMessage();
		// Tìm vị trí trống tiếp theo trong hàng được chỉ định
		else {
			int y = 0;
			while(pieces[row - 1][y] != null) {
				y++;
			}
			// Đặt giá trị cho quân cờ là true hoặc false tùy thuộc vào lượt của ai
			pieces[row-1][y] = redsTurn;
			// Thêm nước đi vào danh sách các nước đi
			moves.push(new Point(row - 1, y));
			// Chuyển lượt
			redsTurn = !redsTurn;
			// Kiểm tra xem nước đi có dẫn đến chiến thắng không
			checkForWin();
		}
	}
	
	// Đặt lại thông báo lỗi, hủy bỏ nước đi cuối cùng và chuyển lượt trở lại
	// Nếu không có nước đi để hủy, đặt thông báo lỗi tương ứng
	public void undo() {
		error = null;
		// Không thể hủy nếu đây là nước đi đầu tiên
		if (moves.empty()) setErrorMessage();
		else {
			// Loại bỏ nước đi cuối cùng khỏi danh sách nước đi
			Point lastMove = moves.pop();
			// Loại bỏ nước đi cuối cùng khỏi bảng
			pieces[(int)lastMove.getX()][(int)lastMove.getY()] = null;
			// Chuyển lượt trở lại
			redsTurn = !redsTurn;
			// Đặt lại trạng thái kết thúc nếu nước đi trước đó đã dẫn đến chiến thắng
			if(gameOver) {
				gameOver = false;
				redWins = false;
				yellowWins = false;
			}
		}
	}
	
	// Đặt lại bảng về trạng thái ban đầu
	public void restart() {
		pieces = new Boolean[7][6];
		gameOver = false;
		redWins = false;
		yellowWins = false;
		redsTurn = true;
		moves.clear();
		error = null;
	}
	
	// Kiểm tra xem có chiến thắng hay không và đặt giá trị chiến thắng tương ứng
	private void checkForWin() {
		gameOver = false;
		redWins = false;
		yellowWins = false;
		// Kiểm tra chiến thắng của đỏ
		if(fourInARow(true)) {
			gameOver = true;
			redWins = true;
		}
		// Kiểm tra chiến thắng của vàng
		else if(fourInARow(false)) {
			gameOver = true;
			yellowWins = true;
		}
		// Kiểm tra xem có hòa không
		else {
			// Nếu không còn ô trống ở hàng đỉnh (và chưa có ai chiến thắng) thì là hòa
			for(int i = 0; i < pieces.length; i++) {
				if(pieces[i][pieces[0].length - 1] == null) break;
				else if(i == pieces.length - 1) gameOver = true;
			}
		}
	}
	
	// Kiểm tra bảng để tìm tất cả các kết hợp 4 liên tiếp của một màu cụ thể
	// true kiểm tra cho đỏ, false kiểm tra cho vàng
	private boolean fourInARow(boolean color) {
		// Dọc
		for(int i = 0; i < pieces.length; i++) {
			int count = 0;
			for(int j = 0; j < pieces[0].length; j++) {
				if(pieces[i][j] == null) break;
				else if(pieces[i][j] == color) count ++;
				else count = 0;
				if(count == 4) {
					return true;
				}
			}
		}
		// Ngang
		for(int j = 0; j < pieces[0].length; j++) {
			int count = 0;
			for(int i = 0; i < pieces.length; i++) {
				if(pieces[i][j] == null || pieces[i][j] == !color) count = 0;
				else count++;
				if(count == 4) {
					return true;
				}
				if(4 - (pieces.length) - 1 - i > count) break;
			}
		}
		// Chéo (\)
		for(int i = 3; i < pieces.length + 2; i++) {
			int count = 0;
			for(int j = 0; (i - j) >= 0 && j < pieces[0].length; j++) {
				if(i - j < pieces.length) {
					if(pieces[i - j][j] == null || pieces[i - j][j] == !color) count = 0;
					else count++;
					if(count == 4) {
						return true;
					}
					if(4 - (pieces[0].length) - 1 - i > count) break;
				}
			}
		}
		// Chéo (/)
		for(int i = 3; i >= -2; i--) {
			int count = 0;
			for(int j = 0; (i + j) < pieces.length && j < pieces[0].length; j++) {
				if(i + j >= 0) {
					if(pieces[i + j][j] == null || pieces[i + j][j] == !color) count = 0;
					else count++;
					if(count == 4) {
						return true;
					}
					if(4 - (pieces[0].length) - 1 - i > count) break;
				}
			}
		}
		// Nếu không tìm thấy kết hợp 4 liên tiếp, trả về false
		return false;
	}
	
	// Thực hiện nước đi cho máy (AI)
	public void moveAI() {
	    if (!gameOver && !redsTurn) {
	        int bestMove = minimax(3, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
	        if (bestMove != -1) {
	            move(bestMove + 1);
	        }
	    }
	}

	

	private int minimax(int depth, int alpha, int beta, boolean maximizingPlayer) {
	    if (depth == 0 || gameOver) {
	        if (gameOver) {
	            if (redWins) {
	                return 100000000;
	            } else if (yellowWins) {
	                return -100000000;
	            } else {
	                return 0;
	            }
	        } else {
	            return scorePosition();
	        }
	    }

	    if (maximizingPlayer) {
	        int value = Integer.MIN_VALUE;
	        int column = -1;

	        for (int col = 0; col < pieces.length; col++) {
	            if (pieces[col][pieces[0].length - 1] == null) {
	                int row = getTopEmptyRow(col);
	                pieces[col][row] = redsTurn;

	                if (fourInARow(true)) {
	                    pieces[col][row] = null; // undo move
	                    return 100000000;
	                }

	                int newScore = minimax(depth - 1, alpha, beta, false);
	                pieces[col][row] = null; // undo move

	                if (newScore > value) {
	                    value = newScore;
	                    column = col;
	                }

	                alpha = Math.max(alpha, value);

	                if (alpha >= beta) {
	                    break;
	                }
	            }
	        }

	        if (depth == 3) {
	            return column;
	        }

	        return value;
	    } else {
	        int value = Integer.MAX_VALUE;

	        for (int col = 0; col < pieces.length; col++) {
	            if (pieces[col][pieces[0].length - 1] == null) {
	                int row = getTopEmptyRow(col);
	                pieces[col][row] = !redsTurn;

	                if (fourInARow(false)) {
	                    pieces[col][row] = null; // undo move
	                    return -100000000;
	                }

	                int newScore = minimax(depth - 1, alpha, beta, true);
	                pieces[col][row] = null; // undo move

	                value = Math.min(value, newScore);

	                beta = Math.min(beta, value);

	                if (alpha >= beta) {
	                    break;
	                }
	            }
	        }

	        return value;
	    }
	}

    private int scorePosition() {
        int[][] board = new int[7][6];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[0].length; j++) {
                if (pieces[i][j] != null) {
                    board[i][j] = pieces[i][j] ? 1 : 2; // 1 for red, 2 for yellow
                }
            }
        }

        // Rest of your existing scoring logic
        // You can use the existing scoring logic you provided earlier

        int piece = redsTurn ? 1 : 2;
        int score = 0;

        // Your existing scoring logic goes here...

        return score;
    }

	// Hàm bổ trợ để lấy hàng trống ở cột cụ thể
	private int getTopEmptyRow(int col) {
	    int row = 0;
	    while (pieces[col][row] != null) {
	        row++;
	    }
	    return row;
	}


	// Đặt thông báo lỗi sau khi thực hiện nước đi không hợp lệ
	private void setErrorMessage() {
		if(gameOver) error = "Trò chơi đã kết thúc.";
		else if(moves.empty()) error = "Không có nước đi để hủy.";
		else error =  "Hàng đó đã đầy.";
	}
	
	public Boolean[][] getPieces(){
		return pieces;
	}
	
	public Stack<Point> getMoves(){
		return moves;
	}
	
	public String getError() {
		return error;
	}
	
	public boolean getRedWins(){
		return redWins;
	}
	
	public boolean getYellowWins(){
		return yellowWins;
	}
	
	public boolean getRedsTurn(){
		return redsTurn;
	}
	
	public boolean getGameOver(){
		return gameOver;
	}
}
