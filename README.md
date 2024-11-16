# Halma Game Solver
AI Course Project  

This repository contains the implementation of the Halma game for a two-player setup, designed as part of an AI course project. The focus of this project is on modeling the game's rules and strategy, implementing the Minimax algorithm for optimal decision-making, and enhancing the evaluation function for improved gameplay.  

---

## **Game Overview**  

Halma is a strategic board game played on an `8x8` or `16x16` grid. In a two-player game, players aim to move all their pieces from their starting corner (camp) to the opponent’s corner. The first player to achieve this wins.  

### **Basic Rules**:  
- **Camps**: Each player starts with a cluster of pieces in one corner of the board.  
- **Movement**:  
  - Pieces can move to an adjacent empty cell or jump over other pieces.  
  - Multiple jumps are allowed in one turn, provided valid moves are available.  
- **Winning Condition**: A player wins by completely occupying the opponent’s camp with their pieces.  

---

## **Project Details**  

### **Key Features**:  
1. **Game Implementation**:  
   - Full implementation of Halma game rules, including movement and jump mechanics.  
2. **AI Strategy**:  
   - Minimax algorithm used to calculate the best move for a player, with depth-based search for optimal strategies.  
   - Alpha-beta pruning applied to improve efficiency by reducing the search space.  
3. **Evaluation Function**:  
   - Optimized to prioritize moves that progress pieces toward the opponent's camp while maintaining defensive positions.  
   - Accounts for proximity to the goal, piece clustering, and blocking opponent moves.  

---

## **Algorithms and Techniques**  

- **Minimax Algorithm**: Determines the best move by simulating all possible outcomes.  
- **Alpha-Beta Pruning**: Reduces the number of nodes evaluated in the Minimax algorithm, improving performance.  
- **Evaluation Function**: Measures the board state to prioritize strategic moves, focusing on progress toward the goal and positional advantage.  

---

## **Results and Analysis**  

- The AI demonstrates effective decision-making with optimized strategies, outperforming random and greedy approaches.  
- The evaluation function enhances gameplay by balancing offensive and defensive strategies.  
