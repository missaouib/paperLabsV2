import { BlocEntity } from 'app/entities/BlocEntity';

export class StepEntity {
  id: string;
  title: string;
  description: string;
  visibility: boolean;
  blocs: BlocEntity[];
}
