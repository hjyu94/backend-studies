import { Body, Controller, Get, Post } from '@nestjs/common';
import { Board } from './boards.model';
import { BoardsService } from './boards.service';

@Controller('boards')
export class BoardsController {
  constructor(private boardsService: BoardsService) {}

  @Get('/')
  getAllBoard(): Board[] {
    return this.boardsService.getAllBoards();
  }

  @Post('/')
  createBoard(
    @Body('title') title: string,
    @Body('description') description: string,
    // @Body() body: any, // request body 전체를 받아 올 수도 있다.
  ) {
    return this.boardsService.createBoard(title, description);
  }
}
