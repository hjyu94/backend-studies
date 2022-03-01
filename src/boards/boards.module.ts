import { Module } from '@nestjs/common';
import { BoardsController } from './boards.controller';
import { BoardsService } from './boards.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { BoardsRepository } from './boards.repository';

@Module({
  imports: [TypeOrmModule.forFeature([BoardsRepository])],
  controllers: [BoardsController],
  providers: [BoardsService],
})
export class BoardsModule {}
