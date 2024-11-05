import type { EmoticonPackInList } from './emoticonPackInList';

interface Sort {
  sorted: boolean;
  empty: boolean;
  unsorted: boolean;
}

interface Pageable {
  paged: boolean;
  pageNumber: number;
  pageSize: number;
  offset: number;
  sort: Sort;
  unpaged: boolean;
}

interface EmoticonPackSearchList {
  totalElements: number;
  totalPages: number;
  pageable: Pageable;
  size: number;
  content: EmoticonPackInList[];
  number: number;
  sort: Sort;
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export type {
  EmoticonPackSearchList,
  Pageable,
  Sort
};
