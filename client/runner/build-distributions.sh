#!/bin/bash

function die {
    declare MSG="$@"
    echo -e "$0: Error: $MSG">&2
    exit 1
}


DIST_DIR="./dist"
if [ -d $DIST_DIR ] ; then
    rm -rf $DIST_DIR/*
else
    mkdir $DIST_DIR
fi

#  GOOS=linux GOARCH=ppc64 go build

for line in `go tool dist list` ; do
    arrIN=(${line//\// })
    os=${arrIN[0]}
    arch=${arrIN[1]}
    
    if [ "$os" != "windows" ] && [ "$os" != "linux" ]; then
        echo "ignoring os $os"
        continue
    fi

    echo "building $os $arch"

    exec_dir=$DIST_DIR/$os/$arch 
    mkdir -p $exec_dir || die "can't run mkdir"

    output_file="runner"
    if [ $os == "windows" ]; then
        output_file="runner.exe"
    fi

    GOOS=$os GOARCH=$arch go build -o $output_file || die "can't run go build"

    mv $output_file $exec_dir || die "can't move $output_file to $exec_dir"
done

