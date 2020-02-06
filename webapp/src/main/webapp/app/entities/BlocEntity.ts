import { Input } from 'app/entities/Input';

export class BlocEntity {
  id: string;
  title: string;
  description: string;
  visibility: boolean;
  inputs: Input[];
}
