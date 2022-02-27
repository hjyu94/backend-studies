import { Injectable } from '@nestjs/common';
import { Board, BoardStatus } from './boards.model';
import { v1 as uuid } from 'uuid';

@Injectable()
export class BoardsService {
  private borads: Board[] = [];

  getAllBoards(): Board[] {
    return this.borads;
  }

  createBoard(title: string, description: string) {
    const board: Board = {
      id: uuid(),
      title, // === title: title
      description,
      status: BoardStatus.PUBLIC,
    };

    this.borads.push(board);
    return board;
  }
}
