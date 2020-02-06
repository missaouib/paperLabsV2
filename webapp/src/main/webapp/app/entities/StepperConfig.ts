import { Step } from 'app/entities/Step';
import { Input } from 'app/entities/Input';
import { Bloc } from 'app/entities/Bloc';
import { Bookmark } from 'app/entities/Bookmark';

export class StepperConfig {
  steps: Step[];
  inputs: Input[];
  blocs: Bloc[];
  bookmarks: Bookmark[];
}
