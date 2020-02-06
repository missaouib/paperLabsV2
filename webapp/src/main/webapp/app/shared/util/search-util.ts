import { Injectable } from '@angular/core';

@Injectable()
export class SearchUtil {
  constructor() {}

  /**
   * Filter array by string
   *
   * @param mainArr
   * @param searchText
   * @returns {any}
   */
  public static filterArrayByString(mainArr, searchText): any {
    if (searchText.length === 0) {
      return mainArr;
    } else {
      return mainArr.filter(itemObj => {
        let found = false;
        searchText = searchText != null ? searchText.toLowerCase() : '';
        if (this.searchInObj(itemObj, searchText)) {
          found = true;
        }
        return found;
      });
    }
  }

  /**
   * Search in object
   *
   * @param itemObj
   * @param searchText
   * @returns {boolean}
   */
  public static searchInObj(itemObj, searchText): boolean {
    for (const prop in itemObj) {
      if (!Object.prototype.hasOwnProperty.call(itemObj, prop)) {
        continue;
      }

      const value = itemObj[prop];

      if (typeof value === 'string') {
        if (this.searchInString(value, searchText)) {
          return true;
        }
      } else if (Array.isArray(value)) {
        if (this.searchInArray(value, searchText)) {
          return true;
        }
      } else if (typeof value === 'number') {
        if (this.searchInNumber(value, searchText)) {
          return true;
        }
      }
      if (typeof value === 'object') {
        if (this.searchInObj(value, searchText)) {
          return true;
        }
      }
    }
  }

  /**
   * Search in array
   *
   * @param arr
   * @param searchText
   * @returns {boolean}
   */
  public static searchInArray(arr, searchText): boolean {
    for (const value of arr) {
      if (typeof value === 'string') {
        if (this.searchInString(value, searchText)) {
          return true;
        }
      }

      if (typeof value === 'object') {
        if (this.searchInObj(value, searchText)) {
          return true;
        }
      }
    }
  }

  /**
   * Search in string
   *
   * @param value
   * @param searchText
   * @returns {any}
   */
  public static searchInString(value, searchText): any {
    return value.toLowerCase().includes(searchText);
  }

  /**
   * Search in number
   *
   * @param value
   * @param searchText
   * @returns {any}
   */
  public static searchInNumber(value, searchText): any {
    return value.toString().includes(searchText.toString());
  }
}
