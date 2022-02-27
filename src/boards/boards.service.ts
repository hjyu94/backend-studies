import { Injectable } from '@nestjs/common';
import { Board, BoardStatus } from './boards.model';
import { v1 as uuid } from 'uuid';
import { CreateBoardDto } from './dto/create-board.dto';

@Injectable()
export class BoardsService {
  private borads: Board[] = [];

  getAllBoards(): Board[] {
    return this.borads;
  }

  createBoard(createBoardDto: CreateBoardDto) {
    const board: Board = {
      id: uuid(),
      title: createBoardDto.title,
      description: createBoardDto.description,
      status: BoardStatus.PUBLIC,
    };

    this.borads.push(board);
    return board;
  }

  getBoardById(id: string): Board {
    return this.borads.find((board) => board.id === id);
  }

  deleteBoard(id: string): void {
    this.borads = this.borads.filter((board) => board.id !== id);
  }
}
