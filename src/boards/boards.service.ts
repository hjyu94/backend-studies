import { Injectable } from '@nestjs/common';

@Injectable()
export class BoardsService {
  private borads = [];

  getAllBoards() {
    return this.borads;
  }
}
