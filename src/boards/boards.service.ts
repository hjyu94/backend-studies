import { Injectable, NotFoundException } from '@nestjs/common';
import { CreateBoardDto } from './dto/create-board.dto';
import { BoardsRepository } from './boards.repository';
import { InjectRepository } from '@nestjs/typeorm';
import { Board } from './board.entity';
import { BoardStatus } from './board-status.enum';

@Injectable()
export class BoardsService {
  constructor(
    @InjectRepository(BoardsRepository)
    private boardsRepository: BoardsRepository,
  ) {}

  // getAllBoards(): Board[] {
  //   return this.boards;
  // }

  createBoard(createBoardDto: CreateBoardDto): Promise<Board> {
    return this.boardsRepository.createBoard(createBoardDto);
  }

  async getBoardById(id: number): Promise<Board> {
    const found = await this.boardsRepository.findOne(id);

    if (!found) {
      throw new NotFoundException(`Can't find Board with id ${id}`);
    }

    return found;
  }

  async deleteBoard(id: number): Promise<void> {
    const result = await this.boardsRepository.delete(id);

    if (result.affected === 0) {
      throw new NotFoundException(`Can't find Board with id ${id}`);
    }
  }

  async updateBoardStatus(id: number, status: BoardStatus): Promise<Board> {
    const board = await this.getBoardById(id);
    board.status = status;
    await this.boardsRepository.save(board);
    return board;
  }
}
